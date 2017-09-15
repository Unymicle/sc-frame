package com.banling.sc.client.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banling.sc.client.service.FeignClientService;
import com.banling.sc.module.User;

@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private FeignClientService feignClientService;
	
	/**简单测试
	 * @param msg
	 * @return
	 */
	@GetMapping("/test/{msg}")
	public String test(@PathVariable String msg){
		return feignClientService.getMsg(msg);
	}
	
	/**代理下载。从Server下载，再提供给消费端下载
	 * @param fileid, String
	 */
	@GetMapping("/download/{fileid}")
	public void download(@PathVariable String fileid,HttpServletResponse response){
		try {
			OutputStream out=new BufferedOutputStream(response.getOutputStream());
			
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String("pic.jpg".getBytes("utf-8"), "iso8859-1"));
			
			Map<String,Object> params=new HashMap<String,Object>();
	        params.put("fileid", fileid);
	        byte[] buffer=null;
	        
	        buffer=feignClientService.downloadFile(fileid);
	        out.write(buffer);
	        out.flush();

	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@GetMapping("/norestapi")
	public String noRestApi(){
		return feignClientService.noRestApi();
	}
	
	@GetMapping("/userList/{count}")
	public List<User> getUserList(@PathVariable Integer count){
		return feignClientService.getUserList(count);
	}
}