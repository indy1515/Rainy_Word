package com.indyzalab.rainywords.gameplay;

import org.json.simple.JSONObject;

public class CommandHelper {

	@SuppressWarnings("unchecked")
	public static JSONObject getCommandDataJSON(String command,Object data){
		JSONObject jObj = new JSONObject();
		jObj.put(CommandConstants.COMMAND, command);
		jObj.put(CommandConstants.DATA, data);
		return jObj;
	}
}
