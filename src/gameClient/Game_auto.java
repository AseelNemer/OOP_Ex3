package gameClient;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.Fruit;
import oop_dataStructure.OOP_DGraph;

public class Game_auto {

		public static void GamePlay(int scenario) {
			
			

			if(scenario<0 || scenario>23) return ;
			game_service game = Game_Server.getServer(scenario); // you have [0,23] games
			String g = game.getGraph();
			OOP_DGraph gg = new OOP_DGraph();
			gg.init(g);
			String info = game.toString();
			//System.out.println(info);
			JSONObject line;
			try {
				line = new JSONObject(info);
				JSONObject ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("robots");
				System.out.println(info);
				System.out.println(g);
				// the list of fruits should be considered in your solution
				Iterator<String> f_iter = game.getFruits().iterator();
				while(f_iter.hasNext()) {
					String s=f_iter.next();
					Fruit fruit=new Fruit();
					fruit.init(s);
					System.out.println(f_iter.next());}	
				int src_node = 0;  // arbitrary node, you should start at one of the fruits
				for(int a = 0;a<rs;a++) {
					game.addRobot(src_node+a);
				}
			}
			catch (JSONException e) {e.printStackTrace();}
			game.startGame();
			// should be a Thread!!!
			//while(game.isRunning()) {
				//moveRobots(game, gg);
			//}
			String results = game.toString();
			System.out.println("Game Over: "+results);
		}
		}

