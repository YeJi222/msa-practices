package com.poscodx.mysite.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.poscodx.mysite.vo.GuestbookVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SuppressWarnings("unchecked")
@RequestMapping("/api/guestbook")
public class GuestbookApiController {
	private final RestTemplate restTemplate;
	
	public GuestbookApiController(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping
	public ResponseEntity<?> list(
			@RequestParam(value="no", required=true, defaultValue="0") Long startNo) {
		log.info("Request[GET /api/guestbook]:"+ startNo);
		
		Map<String, Object> response = restTemplate.getForObject("http://service-mysite08/?no=" + startNo, Map.class);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(response);
	}

	@PostMapping
	public ResponseEntity<?> add(@RequestBody GuestbookVo vo) {
		log.info("Request[POST /api/guestbook]:" + vo);
		
		Map<String, Object> response = restTemplate.postForObject("http://service-mysite08/", vo, Map.class);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(response);
	}

	@DeleteMapping("/{no}")
	public ResponseEntity<?> delete(@PathVariable("no") Long no, @RequestParam(value="password", required=true, defaultValue="") String password) {
		log.info("Request[DELETE /api/guestbook/{no}]:" + no);
		// log.info("clients - no : " + no + ", password : " + password);
		
		URI uri = UriComponentsBuilder.fromUriString("http://service-mysite08/{no}")
                // .queryParam("password", password)
                .buildAndExpand(no)
                .toUri();
		
		// Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Body
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("password", password);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		
		ResponseEntity<?> response = restTemplate.exchange(uri, HttpMethod.DELETE, requestEntity, Map.class);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(response.getBody());
	}
}