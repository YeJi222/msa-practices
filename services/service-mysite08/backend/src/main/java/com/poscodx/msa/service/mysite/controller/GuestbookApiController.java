package com.poscodx.msa.service.mysite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poscodx.msa.service.mysite.dto.JsonResult;
import com.poscodx.msa.service.mysite.service.GuestbookService;
import com.poscodx.msa.service.mysite.vo.GuestbookVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class GuestbookApiController {
	private final GuestbookService guestbookService;

	public GuestbookApiController(GuestbookService guestbookService) {
		this.guestbookService = guestbookService;
	}

	@GetMapping
	public ResponseEntity<?> list(@RequestParam(value="no", required=true, defaultValue="0") Long startNo) {
		log.info("Request[GET /]:"+ startNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(guestbookService.getContentsList(startNo)));
	}

	@PostMapping
	public ResponseEntity<?> add(@RequestBody GuestbookVo vo) {
		log.info("Request[POST /]:" + vo);
		
		guestbookService.addContents(vo);
		vo.setPassword("");		
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(vo));
	}

	@DeleteMapping("/{no}")
	public ResponseEntity<?> delete(@PathVariable("no") Long no, @RequestParam(value="password", required=true, defaultValue="") String password) {
		log.info("Request[DELETE /]:" + no);
		
		Boolean result = guestbookService.deleteContents(no, password);
		System.out.println(result);
		return ResponseEntity.status(HttpStatus.OK).body(JsonResult.success(result ? no : null));
	}
}