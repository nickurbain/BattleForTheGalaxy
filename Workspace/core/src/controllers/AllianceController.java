package controllers;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import battle.galaxy.AllianceScreen;
import data.AllianceData;
import data.JsonHeader;
import master.classes.MasterScreen;

public class AllianceController extends MasterScreen {

	static DataController dc = new DataController(getGame());
	private static AllianceData query;
	
	public static void AllianceQuery(String alliance_name, String user_name, String type) {

		int json_type = JsonHeader.C_TYPE_CREATE_ALLIANCE;

		if (type.equals("join")) {
			json_type = JsonHeader.C_TYPE_JOIN_ALLIANCE;
		}

		query = new AllianceData(JsonHeader.ORIGIN_CLIENT, json_type, alliance_name, user_name);

		if (((String) dc.sendToServerWaitForResponse(query, true)).contains("Successful")) {
			try {
				getGame().setScreen(new AllianceScreen());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			if (json_type == JsonHeader.C_TYPE_JOIN_ALLIANCE) {
				System.out.println("You are already in an alliance");
			} else if (json_type == JsonHeader.C_TYPE_CREATE_ALLIANCE) {
				System.out.println("Alliance name is taken, please try another");
			}
		}

	}
	
	public ArrayList<String> getAllianceNames() {
		ArrayList<String> names = new ArrayList<>();
		query = new AllianceData(JsonHeader.ORIGIN_CLIENT, JsonHeader.C_TYPE_RETRIEVE_ALLIANCES);
		String allianceNames = (String) dc.sendToServerWaitForResponse(query, true);
		allianceNames = allianceNames.substring(14);
		Scanner s = new Scanner(allianceNames);
		s.useDelimiter(",");
		while (s.hasNext()) {
			String temp = s.next();
			if (temp.endsWith("}")) {
				temp = temp.substring(0, temp.length()-2);
			}
			System.out.println("Temp var: " + temp);
			names.add(temp);
		}
		return names;
	}
}