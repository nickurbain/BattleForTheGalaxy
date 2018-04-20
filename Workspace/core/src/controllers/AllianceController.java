package controllers;

import java.net.UnknownHostException;

import battle.galaxy.AllianceScreen;
import data.AllianceData;
import data.JsonHeader;
import master.classes.MasterScreen;

public class AllianceController extends MasterScreen {

	static DataController dc = new DataController(getGame());

	public static void AllianceQuery(String alliance_name, String user_name, String type) {

		int json_type = JsonHeader.C_TYPE_CREATE_ALLIANCE;

		if (type.equals("join")) {
			json_type = JsonHeader.C_TYPE_JOIN_ALLIANCE;
		}

		AllianceData joinOrCreate = new AllianceData(JsonHeader.ORIGIN_CLIENT, json_type, alliance_name, user_name);

		if (((String) dc.sendToServerWaitForResponse(joinOrCreate, true)).contains("Successful")) {
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
}