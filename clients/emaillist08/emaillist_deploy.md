### jenkins - Publish Over SSH
- macOS에서는 ~/.ssh/environment 파일을 통한 환경 변수 설정이 기본적으로 지원되지 않는다(보안 이슈)
- 따라서, 3가지 방법을 생각해보았다 
- launch.sh 수정하는 방법 2가지, jenkins 수정하는 방법 1가지

#### (1) launch.sh에서 java path를 절대 경로로 지정해준 다음 java 명령어 실행 
```sh
# 변경 전
nohup java -Dspring.profiles.active=production -jar $SCRIPT_DIR/$APPLICATION_NAME.jar >> $SCRIPT_DIR/launch.log &

# 변경 후
nohup /usr/local/poscodx2023/java/bin/java -Dspring.profiles.active=production -jar $SCRIPT_DIR/$APPLICATION_NAME.jar >> $SCRIPT_DIR/launch.log &
```

(전체 코드)
```sh
#! /bin/bash

APPLICATION_NAME=emaillist # jar 이름으로 맞춰주기 
SCRIPT_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
PID=$(ps -ef | grep java | grep $APPLICATION_NAME.jar | awk '{print $2}')

if  [ ! -z "$PID" ] 
then
	echo "stopping [$APPLICATION_NAME]"
	kill -9 $PID
	sleep 10
fi

echo "starting [$APPLICATION_NAME]"
cd $SCRIPT_DIR
nohup /usr/local/poscodx2023/java/bin/java -Dspring.profiles.active=production -jar $SCRIPT_DIR/$APPLICATION_NAME.jar >> $SCRIPT_DIR/launch.log &
```

#### (2) launch.sh에서 환경변수 등록 명령어를 추가
```
export PATH=/usr/local/poscodx2023/java/bin
```

(전체 코드)
```sh
#! /bin/bash

export PATH=/usr/local/poscodx2023/java/bin

APPLICATION_NAME=emaillist # jar 이름으로 맞춰주기 
SCRIPT_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
PID=$(ps -ef | grep java | grep $APPLICATION_NAME.jar | awk '{print $2}')

if  [ ! -z "$PID" ] 
then
	echo "stopping [$APPLICATION_NAME]"
	kill -9 $PID
	sleep 10
fi

echo "starting [$APPLICATION_NAME]"
cd $SCRIPT_DIR
nohup java -Dspring.profiles.active=production -jar $SCRIPT_DIR/$APPLICATION_NAME.jar >> $SCRIPT_DIR/launch.log &
```

#### (3) jenkins에서 
- 빌드 후 조치 > Send build artifacts over SSH > Transfer Set > Exec command
- 아래 명령어를 추가 
```sh
export PATH=/usr/local/poscodx2023/java/bin
```

(전체 명령어)
```sh
chmod 700 /usr/local/poscodx2023/springboot-apps/emaillist07/launch.sh
export PATH=/usr/local/poscodx2023/java/bin
/usr/local/poscodx2023/springboot-apps/emaillist07/launch.sh
```
<img width="683" alt="image" src="https://github.com/YeJi222/Bit_TIL/assets/70511859/2cbb91ef-134f-439b-82d9-a291ef327c48">
