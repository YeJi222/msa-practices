package com.poscodx.msa.service.mysite.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GalleryVo {
	private Long no;
	private String imageUrl;
	private String comment;
}