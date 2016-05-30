/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package traincraft.common.core.handlers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import traincraft.common.Traincraft;
import traincraft.common.library.TCBlocksList;
import traincraft.common.library.ItemIDs;

public class ConfigHandler {

	public static boolean ORE_GEN;
	public static boolean COPPER_ORE_GEN;
	public static boolean ENABLE_ZEPPELIN;
	public static boolean SOUNDS;
	public static boolean FLICKERING;
	public static boolean ENABLE_STEAM;
	public static boolean ENABLE_DIESEL;
	public static boolean ENABLE_ELECTRIC;
	public static boolean ENABLE_BUILDER;
	public static boolean ENABLE_TENDER;
	public static boolean CHUNK_LOADING;
	public static boolean SHOW_POSSIBLE_COLORS;
	public static boolean MYSQL_ENABLE;
	public static int TRAINCRAFT_VILLAGER_ID;
	public static boolean REAL_TRAIN_SPEED;

	public static int Key_Acc = 17; // W
	public static int Key_Dec = 31; // S
	public static int Key_Left = 30; // W
	public static int Key_Right = 32; // S
	public static int Key_Horn = 35; // H
	public static int Key_Invent = 19; // R
	public static int Key_Up = 44; // Y
	public static int Key_Down = 45; // X
	public static int Key_Idle = 46; // C
	public static int Key_Furn = 33; // F
	public static int Key_Brake = 57; // Space

	public static String MYSQL_URL;
	public static String MYSQL_USER;
	public static String MYSQL_PASSWORD;

	private final static String CATEGORY_KEYS = "Keys";
	private final static String CATEGORY_MYSQL = "MYSQL";

	public static void init(File configFile) {
		Configuration cf = new Configuration(configFile);

		try {
			cf.load();
			/* General */
			SOUNDS = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_SOUNDS", true).getBoolean(true);
			FLICKERING = cf.get(Configuration.CATEGORY_GENERAL, "DISABLE_FLICKERING", true).getBoolean(true);
			ORE_GEN = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_FUEL_ORES_SPAWN", true).getBoolean(true);
			COPPER_ORE_GEN = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_COPPER_SPAWN", true).getBoolean(true);
			ENABLE_ZEPPELIN = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_ZEPPELIN", true).getBoolean(true);
			ENABLE_STEAM = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_STEAM_TRAINS", true).getBoolean(true);
			ENABLE_DIESEL = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_DIESEL_TRAINS", true).getBoolean(true);
			ENABLE_ELECTRIC = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_ELECTRIC_TRAINS", true).getBoolean(true);
			ENABLE_BUILDER = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_TRACKS_BUILDER", true).getBoolean(true);
			ENABLE_TENDER = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_TENDERS", true).getBoolean(true);
			CHUNK_LOADING = cf.get(Configuration.CATEGORY_GENERAL, "ENABLE_CHUNK_LOADING", true).getBoolean(true);
			TRAINCRAFT_VILLAGER_ID = cf.get(Configuration.CATEGORY_GENERAL, "TRAINCRAFT_VILLAGER_ID", 86).getInt();
			Property SHOW_POSSIBLE_COLORS_PROP = cf.get(Configuration.CATEGORY_GENERAL, "SHOW_POSSIBLE_TRAINS_COLORS_IN_CHAT", true);
			SHOW_POSSIBLE_COLORS_PROP.comment = "This will disable the chat messages telling you the possible colors when spawning new trains and when coloring them with dye";
			SHOW_POSSIBLE_COLORS = SHOW_POSSIBLE_COLORS_PROP.getBoolean(true);
			REAL_TRAIN_SPEED = cf.get(Configuration.CATEGORY_GENERAL, "REAL_TRAIN_SPEED", false).getBoolean(false);
			
			/* Mysql */
			Property mysqlEnable = cf.get(CATEGORY_MYSQL, "MYSQL_ENABLE", false);
			mysqlEnable.comment = "MySQL logger is ment to log train place, destroy, color, create and explode events to your local MYSQL server. \n" + "This will NOT send any information elsewhere. \n" + "Logged events can be used on webpage (if you know how to program in PhP or any other WEB scripting language) \n" + "to track history of every train or just track, who has done something recently. \n" + "This ONLY works on dedicated servers, ONLY the OWNER of the SERVER must setup the url, the username and password for his mysql server where stats will be sent \n" + "That means this system DOESN'T have access to ANY of the CLIENT informations \n" + "The url will be handled like so in the code: String url ='jdbc:mysql://' +ConfigHandler.MYSQL_URL; \n" + "If you have questions about this code please contact \n" + "Spitfire4466 and/or DragonBornSR (author of the mysql part and owner of thesociety.eu Traincraft server: http://forum.thesociety.eu)";

			MYSQL_ENABLE = mysqlEnable.getBoolean(false);
			MYSQL_URL = cf.get(CATEGORY_MYSQL, "MYSQL_URL", "some url").getString();
			MYSQL_USER = cf.get(CATEGORY_MYSQL, "MYSQL_USER", "your username").getString();
			MYSQL_PASSWORD = cf.get(CATEGORY_MYSQL, "MYSQL_PASSWORD", "your password").getString();
			
			/* Keys */
			Traincraft.proxy.setKeyBinding("Horn", cf.get(CATEGORY_KEYS, "KEY_HORN", Key_Horn).getInt());
			Traincraft.proxy.setKeyBinding("Invent", cf.get(CATEGORY_KEYS, "KEY_INVENT", Key_Invent).getInt());
			Traincraft.proxy.setKeyBinding("Up", cf.get(CATEGORY_KEYS, "KEY_UP", Key_Up).getInt());
			Traincraft.proxy.setKeyBinding("Down", cf.get(CATEGORY_KEYS, "KEY_DOWN", Key_Down).getInt());
			Traincraft.proxy.setKeyBinding("Idle", cf.get(CATEGORY_KEYS, "KEY_IDLE", Key_Idle).getInt());
			Traincraft.proxy.setKeyBinding("Furnace", cf.get(CATEGORY_KEYS, "KEY_FURN", Key_Furn).getInt());

			
		} catch (Exception e) {
			Traincraft.tcLog.severe("Traincraft had a problem loading its configuration\n" + e);
		} finally {
			if(cf.hasChanged()) {
				cf.save();
			}
		}
	}
}
