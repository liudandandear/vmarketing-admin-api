package com.vmarketing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	@RequestMapping("/")
	public String index()
	{
		return "this is vmarketing-admin-api !";
	}
}
