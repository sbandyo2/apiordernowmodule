package com.ibm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ibm.bean.RequisitionDTO;
import com.ibm.bean.VOWrapperDTO;
import com.ibm.input.handler.TransformInput;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class ONServiceController {
	Logger logger = LoggerFactory.getLogger(ONServiceController.class);

	private static final String RECIEVED = "RECIEVEDDATA";
	private static final String JSON = ".json";
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EurekaClient eurekaClient;
	
	@HystrixCommand(fallbackMethod="reliable", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"), //default value ,number of request which will trip the circuit
			@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="25000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "25000") })
	

	@RequestMapping(value = "/ordernow", method = RequestMethod.POST)
	public String processTransaction(@RequestBody String json) {
		logger.info("Starting Order now Transaction ");
		
		InstanceInfo instanceInfo = null;
		String url = null;
		String response = null;
		Application bakendApplication = null;
		Application aribaApplication = null;
		TransformInput transformInput = null;
		VOWrapperDTO voWrapperDTO = null;
		RequisitionDTO  requisitionDTO = null;
		
		transformInput = new TransformInput();
		requisitionDTO = transformInput.transformInput(json);
		
		//perform transaction transform and update in Ariba service 
		bakendApplication = eurekaClient.getApplication("backend-service");
		instanceInfo = bakendApplication.getInstances().get(0);
		url= "http://" + instanceInfo.getIPAddr() + ":"+ instanceInfo.getPort() + "/" + "/dbattachinsert/";
		
		//saving the received data
		voWrapperDTO = new VOWrapperDTO();
		voWrapperDTO.setRecievedData(new StringBuffer(json));
		voWrapperDTO.setFileName(requisitionDTO.getApplicationType()+"_"+requisitionDTO.getApplicationTransactionNumber()+"_"+RECIEVED);
		voWrapperDTO.setFileType(JSON);
		restTemplate.postForObject(url, voWrapperDTO, String.class);
		
		//perform transaction transform and update in Ariba service 
		aribaApplication = eurekaClient.getApplication("sapariba-service");
		instanceInfo = aribaApplication.getInstances().get(0);
		url = "http://" + instanceInfo.getIPAddr() + ":"+ instanceInfo.getPort() + "/" + "/ariba/";
		
		response = restTemplate.postForObject(url,requisitionDTO , String.class);

		logger.info("Finishing order now transaction ");
		
		return response.toString();
	}

	
	@RequestMapping(value = "/getSuppPartneringInfo", method = RequestMethod.POST)
	public String getSuppPartneringInfo(@RequestBody String supplierIds) {
		logger.info("Starting Order now Transaction ");
		
		InstanceInfo instanceInfo = null;
		String url = null;
		String response = null;
		Application aribaApplication = null;
		
		//perform transaction to backend service 
		aribaApplication = eurekaClient.getApplication("backend-service");
		instanceInfo = aribaApplication.getInstances().get(0);
		url = "http://" + instanceInfo.getIPAddr() + ":"+ instanceInfo.getPort() + "/" + "/getSuppPartneringInfo/";
		
		response = restTemplate.postForObject(url,supplierIds, String.class);

		logger.info("Finishing order now transaction ");
		
		return response.toString();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	
	public String reliable(@RequestBody String recievedData ) {
		
		logger.info(" Falling back for ON Transaction ");
		
		String response = "<returnData><error>Circuit broke at Ariba Service for ON transaction :"+new TransformInput().transformInput(recievedData).getApplicationTransactionNumber()+"</error></returnData>";


		logger.info("Finishing Fall back ON  transaction ");
		
		return response.toString();
	}
	
}
