package Core;

import java.util.ArrayList;

public class PawnGenerator {
	private static ArrayList<Pawn> pawn = new ArrayList<Pawn>();
	
	public static ArrayList<Pawn> generatePawnsHnefatafl(Player p1, Player p2) {
		
		/***Player 1's pawns****/
		
		pawn.add(new Pawn(0,3,p1));
		pawn.add(new Pawn(0,4,p1));
		pawn.add(new Pawn(0,5,p1));
		pawn.add(new Pawn(0,6,p1));
		pawn.add(new Pawn(0,7,p1));
		pawn.add(new Pawn(1,5,p1));
		pawn.add(new Pawn(3,0,p1));
		pawn.add(new Pawn(3,10,p1));
		pawn.add(new Pawn(4,0,p1));
		pawn.add(new Pawn(4,10,p1));
		pawn.add(new Pawn(5,0,p1));
		pawn.add(new Pawn(5,1,p1));
		pawn.add(new Pawn(5,9,p1));
		pawn.add(new Pawn(5,10,p1));
		pawn.add(new Pawn(6,0,p1));
		pawn.add(new Pawn(6,10,p1));
		pawn.add(new Pawn(7,0,p1));
		pawn.add(new Pawn(7,10,p1));
		pawn.add(new Pawn(9,5,p1));
		pawn.add(new Pawn(10,3,p1));
		pawn.add(new Pawn(10,4,p1));
		pawn.add(new Pawn(10,5,p1));
		pawn.add(new Pawn(10,6,p1));
		pawn.add(new Pawn(10,7,p1));
		
		/************************/
		
		/***Player 2's pawns****/
		
		pawn.add(new Pawn(5,3,p2));
		pawn.add(new Pawn(4,4,p2));
		pawn.add(new Pawn(5,4,p2));
		pawn.add(new Pawn(6,4,p2));
		pawn.add(new Pawn(3,5,p2));
		pawn.add(new Pawn(4,5,p2));
		pawn.add(new Pawn(5,5,p2));
		pawn.add(new Pawn(6,5,p2));
		pawn.add(new Pawn(7,5,p2));
		pawn.add(new Pawn(4,6,p2));
		pawn.add(new Pawn(5,6,p2));
		pawn.add(new Pawn(6,6,p2));
		pawn.add(new Pawn(5,7,p2));
		
		pawn.get(30).setKing(true);
		/************************/
		
		return pawn;
	}
	
	public static ArrayList<Pawn> generatePawnsTablut(Player p1, Player p2) {
		
	/***Player 1's pawns****/
		
		pawn.add(new Pawn(0,3,p1));
		pawn.add(new Pawn(0,4,p1));
		pawn.add(new Pawn(0,5,p1));
		pawn.add(new Pawn(1,4,p1));
		pawn.add(new Pawn(3,0,p1));
		pawn.add(new Pawn(3,8,p1));
		pawn.add(new Pawn(4,0,p1));
		pawn.add(new Pawn(4,8,p1));
		pawn.add(new Pawn(5,0,p1));
		pawn.add(new Pawn(4,1,p1));
		pawn.add(new Pawn(4,7,p1));
		pawn.add(new Pawn(5,8,p1));
		pawn.add(new Pawn(7,4,p1));
		pawn.add(new Pawn(8,3,p1));
		pawn.add(new Pawn(8,4,p1));
		pawn.add(new Pawn(8,5,p1));
		
		/************************/
		
		/***Player 2's pawns****/
		
		pawn.add(new Pawn(4,2,p2));
		pawn.add(new Pawn(4,3,p2));
		pawn.add(new Pawn(2,4,p2));
		pawn.add(new Pawn(3,4,p2));
		pawn.add(new Pawn(4,4,p2));
		pawn.add(new Pawn(5,4,p2));
		pawn.add(new Pawn(6,4,p2));
		pawn.add(new Pawn(4,5,p2));
		pawn.add(new Pawn(4,6,p2));
		
		pawn.get(20).setKing(true);
		/************************/
		
		return pawn;
	}
	
	public static ArrayList<Pawn> generatePawnsTawlbawrdd(Player p1, Player p2) {
		
		/***Player 1's pawns****/
			
			pawn.add(new Pawn(4,0,p1));
			pawn.add(new Pawn(5,0,p1));
			pawn.add(new Pawn(6,0,p1));
			pawn.add(new Pawn(4,1,p1));
			pawn.add(new Pawn(5,1,p1));
			pawn.add(new Pawn(6,1,p1));
			pawn.add(new Pawn(0,4,p1));
			pawn.add(new Pawn(1,4,p1));
			pawn.add(new Pawn(9,4,p1));
			pawn.add(new Pawn(10,4,p1));
			pawn.add(new Pawn(0,5,p1));
			pawn.add(new Pawn(1,5,p1));
			pawn.add(new Pawn(9,5,p1));
			pawn.add(new Pawn(10,5,p1));
			pawn.add(new Pawn(0,6,p1));
			pawn.add(new Pawn(1,6,p1));
			pawn.add(new Pawn(9,6,p1));
			pawn.add(new Pawn(10,6,p1));
			pawn.add(new Pawn(4,9,p1));
			pawn.add(new Pawn(5,9,p1));
			pawn.add(new Pawn(6,9,p1));
			pawn.add(new Pawn(4,10,p1));
			pawn.add(new Pawn(5,10,p1));
			pawn.add(new Pawn(6,10,p1));
			
			/************************/
			
			/***Player 2's pawns****/
			
			pawn.add(new Pawn(5,2,p2));
			pawn.add(new Pawn(5,3,p2));
			pawn.add(new Pawn(5,4,p2));
			pawn.add(new Pawn(5,6,p2));
			pawn.add(new Pawn(5,7,p2));
			pawn.add(new Pawn(5,8,p2));
			pawn.add(new Pawn(5,5,p2));
			pawn.add(new Pawn(2,5,p2));
			pawn.add(new Pawn(3,5,p2));
			pawn.add(new Pawn(4,5,p2));
			pawn.add(new Pawn(6,5,p2));
			pawn.add(new Pawn(7,5,p2));
			pawn.add(new Pawn(8,5,p2));
			
			pawn.get(30).setKing(true);
			/************************/
			
			return pawn;
		}
	
}
