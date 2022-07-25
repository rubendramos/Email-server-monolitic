package com.example.emailserver.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmailServerUtils {

	private static final String MAIL_ADDRESS_SEPARATOR=","; 
	
	public static Set<String> getAddressSet(String addresString){
		return new HashSet<String>(Arrays.asList(addresString.split(MAIL_ADDRESS_SEPARATOR)));
	}
	
}
