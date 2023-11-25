package com.poscodx.msa.service.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.poscodx.msa.service.storage.httpd.SimpleHttpd;

/**
 * [Thread 사용] 
 * - 실행시, 계속 로딩중인 아이콘을 화살표로 표시할 수 있는 있
 * - 기존 코드에서는 httpd.start()가 현재 실행중인 쓰레드에서 실행되므로, 
 * 	 코드 블록이 실행되는 동안 메인 쓰레드는 이 코드가 완료될 때까지 대기한다 
 * - httpd.start()를 호출하면 해당 메서드의 실행이 끝날 때까지 현재 쓰레드에서 대기한다.
 * 	 이로 인해 애플리케이션 초기화가 느려질 수 있다.
 * 	 반면에 새로운 쓰레드를 사용하면 httpd.start()가 별도의 쓰레드에서 비동기적으로 실행되므로, 
 * 	 다른 초기화 작업들과 병렬로 실행될 수 있다
 */

@SpringBootApplication
@EnableAsync // 비동기적인 메소드 호출 가능 - 아래 코드에선 쓰레드를 직접 생성하기 때문에 없어도 OK
public class ServiceStorage {

	public static void main(String[] args) {
		SpringApplication.run(ServiceStorage.class, args);
	}

	@Bean
	ApplicationRunner scriptRunner() {
		return new ApplicationRunner() {
			@Autowired
			private SimpleHttpd httpd;

			@Override
			public void run(ApplicationArguments args) throws Exception {
				new Thread(() -> { // 새로운 쓰레드 정의
                    try {
                        httpd.start(); // 별도의 쓰레드에서 실행
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
			}
		};
	}
}
