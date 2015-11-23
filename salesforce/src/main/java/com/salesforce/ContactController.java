package com.salesforce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.force.api.DescribeSObject.Field;

@Controller
public class ContactController {

	 @Autowired
	    private LoginService loginService;
	 private static List<String> sObjectFieldNames = null;
	 private static final String SELECT = "SELECT ";
	 private static final String FROM = "FROM ";
	 
	@RequestMapping(value = "/contacts", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Contact>> getContacts() {

		List<Contact> contacts = getDummyContacts();

		return new ResponseEntity<List<Contact>>(contacts, HttpStatus.OK);
	}
	
	@RequestMapping(value="/view", produces = {
			MediaType.TEXT_HTML_VALUE},  
			method = RequestMethod.GET)
	public String viewContacts ( HttpServletRequest request, HttpServletResponse response) {
	//	if(request.getSession().getAttribute(OAuthConstants.ACCESS_TOKEN) != null)
		loginService.showSObjects();
			
			return "contact-listing";
		//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			// return "redirect:/oauth";
		
	}
	
	
	public List<Contact> getDummyContacts() {
		
		List<String> s = loginService.showSObjects();
		List<Contact> contacts = new ArrayList<>();
		for(int i=0;i<s.size();i++){
			//s.get(i);
			contacts.add(new Contact(s.get(i), "202-555-0105", "joe@gmail.com"));
			
		}
		
		//Contact contact1 = new Contact("Joe", "202-555-0105", "joe@gmail.com");
		//Contact contact2 = new Contact("Jenny", "515-876-5309", "jenny@gmail.com");
		//Contact contact3 = new Contact("Brit", "515-555-0127", "brit@gmail.com");
		
		//List<Contact> contacts = new ArrayList<>();
		/*contacts.add(contact1);
		contacts.add(contact2);
		contacts.add(contact3);*/
		
		return contacts;
		
	}
	
	
	//@RequestMapping(value="/query/{sobjectName}", method = RequestMethod.GET)
	//public String querySObjects(@PathVariable("sobjectName") String sObjectName, Map<String, Object> map) throws HttpMessageNotReadableException, IOException
	//{
	
	@RequestMapping(value = "/query/{sobjectName}", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String > getContactsi(@PathVariable("sobjectName") String sObjectName) {
		try
		{
			sObjectFieldNames = new ArrayList<String>();
			StringBuilder buildSoqlQuery = new StringBuilder();
			buildSoqlQuery.append(SELECT);
		
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(false);
			//session.setAttribute("currentSObject", sObjectName);
		
			//sObjectFieldNames.clear();
			for (Field listOfField : loginService.LoginToSalesforce().describeSObject(sObjectName).getFields())
			{	
				buildSoqlQuery.append(listOfField.getName().concat(", "));
				sObjectFieldNames.add(listOfField.getName().toString());
			}
	
			buildSoqlQuery.deleteCharAt(buildSoqlQuery.length()-2);
		
			buildSoqlQuery.append(FROM);
			buildSoqlQuery.append(sObjectName);
		
			//map.put("sobjectFieldNamesSOQL", sObjectFieldNames);
			//map.put("sobjectQuery", buildSoqlQuery.toString());
			return new ResponseEntity<String>(buildSoqlQuery.toString(), HttpStatus.OK);
		} 	catch (Exception e) {
			//map.put("error", e.getMessage());
			
		}
		return null;
	}
	
	
	
	
	
	

}
