package jchess.visitor;

import java.util.ArrayList;
import java.util.List;

import jchess.core.Chessboard;
import jchess.core.Colors;
import jchess.core.Game;
import jchess.core.pieces.Piece;

public class Calculateur implements Calcul {


	/**
	 * Méthode permettant de recenser le nombre de pièce de chaque coté avec le paramètre color 
	 * @color : String -> "white" ou "black"
	 */
	public int[] m1(Game current) {		
		
		int [] nbPiece = new int [6];
		for(int i = 0; i < nbPiece.length; i++){
			nbPiece[i] = 0;
		}
		
		/*
		 * index :    0 1 2 3 4 5 
		 * Symboles : P B N R Q K
		 * 
		*/
		
		ArrayList<Piece> liste = current.getChessboard().getAllPieces(current.getActivePlayer().getColor());
		
		for(int i = 0; i < liste.size(); i++){			
			
			if(liste.get(i).getName().equals("Pawn")){
					nbPiece[0]+=1;
			}
			else if(liste.get(i).getName().equals("Bishop")){
					nbPiece[1]+=1;
			}
			else if(liste.get(i).getName().equals("Knight")){
					nbPiece[2]+=1;
			}
			else if(liste.get(i).getName().equals("Rook")){
					nbPiece[3]+=1;
			}
			else if(liste.get(i).getName().equals("Queen")){
					nbPiece[4]+=1;
			}
			else if(liste.get(i).getName().equals("King")){
					nbPiece[5]+=1;
			}
			else{
				System.out.println("Impossible");
			}
			
		}
		
		System.out.println(current.getActivePlayer().getName() + " : P->"+nbPiece[0] +" B->" + nbPiece[1]+ " N->" + nbPiece[2]+" R->" + nbPiece[3]+" Q->" +nbPiece[4]+" R->" + nbPiece[5]);
		return nbPiece;
	}


	public int m2(Game current) {
		
		int[] tab = m1(current);
		
		int score = tab[0]*1 + tab[1]*3 + tab[2]*3 + tab[3]*5 + tab[4]*10 + tab[5]*1000;
		
		return score;
	}


}
