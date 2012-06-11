package heuristic;
import gui.Board;

import java.io.*;

import logic.Logic;
import main.Config;


//bitmask corresponds to board as follows in 7x6 case:
//.  .  .  .  .  .  .  TOP
//5 12 19 26 33 40 47
//4 11 18 25 32 39 46
//3 10 17 24 31 38 45
//2  9 16 23 30 37 44
//1  8 15 22 29 36 43
//0  7 14 21 28 35 42  BOTTOM


public class HeuristicNOutOfFour implements Heuristic {
	
	public HeuristicNOutOfFour(){
		
		
		
	}
	
	
	@Override
	public int getBestColumn(logic.Board board, int playerID) {
		return getBestSlot(board.getFields(), board.getTopFields(), Config.PLAYER1,Config.PLAYER2, playerID);
	}


	@Override
	public int calcColumnScore(logic.Board board, int x, int playerID) {
		return getSlotValue(board.getFields(), x, board.getTopField(x), Config.PLAYER1, Config.PLAYER2, playerID);
	}
	


private int getSlotValue(int[][] test, int slot, int row, int player1, int player2, int activePlayer){
	
	Long boardforPlayer1 = 0l;   // 1 sec
	Long boardforPlayer2 = 0l;	
	long emptyBoardForPlayer1 = 0l, emptyBoardForPlayer2 = 0l, emptyBoard = 279258638311359l;
	
	if(activePlayer == player1){
		boardforPlayer1 = getBitRepresentation(test, player1);   // 1 sec
		boardforPlayer2 = getBitRepresentation(test, player2);	
	}
	else{
		boardforPlayer1 = getBitRepresentation(test, player2);   // 1 sec
		boardforPlayer2 = getBitRepresentation(test, player1);	
	}
	
	int scoreValue = 0, blockValue  = 0, linesValue = 0;
	
	emptyBoardForPlayer1 = boardforPlayer2^emptyBoard;
	emptyBoardForPlayer2 = boardforPlayer1^emptyBoard;
	
	int freeDescendDiagonalCode = getRelevantDescendDiagonal((emptyBoardForPlayer1), slot, row );
	int freeAscendDiagonalCode  = getRelevantAscendDiagonal ((emptyBoardForPlayer1), slot, row ); 
	int freeRowCode 			= getRelevantRow            ((emptyBoardForPlayer1), slot, row );
	int freeSlotCode 			= getRelevantSlot           ((emptyBoardForPlayer1), slot, row );	
	
	
	scoreValue =  getNOutOfFourValue(freeDescendDiagonalCode, getRelevantDescendDiagonal(boardforPlayer1 , slot , row ), 50, 10, 5);  
	scoreValue += getNOutOfFourValue(freeAscendDiagonalCode,  getRelevantAscendDiagonal (boardforPlayer1 , slot , row ), 50, 10, 5);  
	scoreValue += getNOutOfFourValue(freeRowCode, 			  getRelevantRow            (boardforPlayer1 , slot , row ), 50, 10, 5);  
	scoreValue += getNOutOfFourValue(freeSlotCode, 			  getRelevantSlot           (boardforPlayer1 , slot , row ), 50, 10, 5);  

	linesValue =  getNumberOfWinningLines(freeDescendDiagonalCode); 
	linesValue += getNumberOfWinningLines(freeAscendDiagonalCode);  
	linesValue += getNumberOfWinningLines(freeRowCode);  
	linesValue += getNumberOfWinningLines(freeSlotCode);  
	
	blockValue =  getNOutOfFourValue((getRelevantDescendDiagonal((emptyBoardForPlayer2), slot, row )), getRelevantDescendDiagonal(boardforPlayer2 , slot , row ), 100, 4,1);  // 1.8s
	blockValue += getNOutOfFourValue((getRelevantAscendDiagonal ((emptyBoardForPlayer2), slot, row )), getRelevantAscendDiagonal (boardforPlayer2 , slot , row ), 100, 4,1);  // 2.4s
	blockValue += getNOutOfFourValue((getRelevantRow            ((emptyBoardForPlayer2), slot, row )), getRelevantRow            (boardforPlayer2 , slot , row ), 100, 4,1);  // 2.4s
	blockValue += getNOutOfFourValue((getRelevantSlot           ((emptyBoardForPlayer2), slot, row )), getRelevantSlot           (boardforPlayer2 , slot , row ), 100, 4,1);  // 1.3s
	
	
	return scoreValue + blockValue + linesValue;
}


public int getBestSlot(int[][] test, int freeRows[], int player1, int player2, int activePlayer){
	
	Long boardforPlayer1 = 0l;   // 1 sec
	Long boardforPlayer2 = 0l;	
	long emptyBoard = 0l;
	
	//System.out.println(activePlayer);
	
	if(activePlayer == player1){
		boardforPlayer1 = getBitRepresentation(test, player1);   // 1 sec
		boardforPlayer2 = getBitRepresentation(test, player2);	
	}
	else{
		boardforPlayer1 = getBitRepresentation(test, player2);   // 1 sec
		boardforPlayer2 = getBitRepresentation(test, player1);		
	}	
	
	emptyBoard = boardforPlayer2^279258638311359l;
	
	printBits(boardforPlayer1);
	printBits(boardforPlayer2);
	
	
	int value  = 0;
	int result = 0;
	int highestValue = 0; 
	
	for(int z = 0; z < 7; z++){ // 4 sec
 		value = getNOutOfFourValue ((getRelevantDescendDiagonal((emptyBoard), z, freeRows[z] )), getRelevantDescendDiagonal(boardforPlayer1 , z , freeRows[z] ), 50, 6, 2);  // 1.8s
	    value += getNOutOfFourValue((getRelevantAscendDiagonal ((emptyBoard), z, freeRows[z] )), getRelevantAscendDiagonal (boardforPlayer1 , z , freeRows[z] ), 50, 6, 2);  // 2.4s
    	value += getNOutOfFourValue((getRelevantRow            ((emptyBoard), z, freeRows[z] )), getRelevantRow            (boardforPlayer1 , z , freeRows[z] ), 50, 6, 2);  // 2.4s
     	value += getNOutOfFourValue((getRelevantSlot           ((emptyBoard), z, freeRows[z] )), getRelevantSlot           (boardforPlayer1 , z , freeRows[z] ), 50, 6, 2);  // 1.3s
    	
     	
    	System.out.println("Total value for ("+z+","+freeRows[z]+") is "+getSlotValue(test, z, freeRows[z], player1, player2, activePlayer));

    	
    	if(value > highestValue){
    		highestValue = value;
    		result = z; 
    	}
    	value  = 0;
	}
	return result;
}


private int getNOutOfFourValue(long possible, long owned, int maxVal, int midVal, int minVal) { //2.5s
	int thisChance = 0, result = 0;
	
	for(int i = 0; i < 4; i++){
		if((possible&15) == 15){
			thisChance = 0;
			for(int j = 0; j<4;j++)
				thisChance += (owned>>j)&1;
			
			if(thisChance == 0);
			else if(thisChance == 1) result += minVal;
			else if(thisChance == 2) result += midVal;
			else if(thisChance == 3) result += maxVal;		
			
		}
		possible>>>=1;
		owned>>>=1;
	}
	return result;
}

private int getNumberOfWinningLines(long possible) { //2.5s
	int result = 0;
	
	for(int i = 0; i < 4; i++){
		if((possible&15) == 15){
			result++;
		}
		possible>>>=1;
	}
	return result;
}



/*
0 : 0-6    : lnew = l>>>(height+1*)5 & 127 (111111)  
1 : 7-8
*/
private int getRelevantSlot(long _board, int slot, int row){
	int l =  (int)((_board>>>((Config.dimensionY+1)*(6-slot))) & 127);
	
	l<<=1;
	
	
	
	if(row<3)
	l>>=3-row;
	else
	l<<=row-3;	
	
	
	return l;
}


private int getRelevantRow(long _board, int slot, int row){
	
	long l = _board>>>=((Config.dimensionY-1)-row); // push bit with wanted row to position 0
	int result = 0;
	for(int i = 0; i < Config.dimensionX; i++){
		result<<=1;
		result += (l>>>(i*(Config.dimensionY+1))) & 1; // push one row & get value
	}
		
	if(slot<3)
	result<<=3-slot;
	else
	result>>=slot-3;	
	
	return result&127; // 1111111
}

private int getRelevantDescendDiagonal(long _board, int slot, int row){
	
	long l = 0l;
	int height = Config.dimensionY+1;
	int result = 0;
	
	if (slot >= row){
		l = _board<<((height)*(slot-row)); // shift scanned diagonal from the board -> first potential in left upper edge
		l = (l>>>height);					// last potential right lower edge
	}else
		l = _board>>((height)*(row-slot+1)); // last potential right lower edge


	result += l & 1; // push one row
	result<<=1;
	result += (l>>>8) & 1; // push one row
	result<<=1;
	result += (l>>>16) & 1; // push one row
	result<<=1;
	result += (l>>>24) & 1; // push one row
	result<<=1;
	result += (l>>>32) & 1; // push one row
	result<<=1;
	result += (l>>>40) & 1; // push one row
	result<<=1;
	
	if(row<2)
	result<<=2-row;
	else
	result>>=row-2;	
	
	return result;
}

private int getRelevantAscendDiagonal(long _board, int slot, int row){
	return (getRelevantDescendDiagonal(flipHorizantal(_board),6-slot,row));
}


private long flipHorizantal(long _board) {

	long l = 0l;	
	int height = Config.dimensionY+1;
	
	l |= (_board) & 127; 	  l<<=height;
	l |= (_board>>>=height) & 127; l<<=height;
	l |= (_board>>>=height) & 127; l<<=height;
	l |= (_board>>>=height) & 127; l<<=height;
	l |= (_board>>>=height) & 127; l<<=height;
	l |= (_board>>>=height) & 127; l<<=height;
	l |= (_board>>>=height) & 127;

	return l;
}



// TESTING |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\
/*
public static void main(String[] args) {
	HeuristicNOutOfFour testMe = new HeuristicNOutOfFour();
//	testMe.runPackingTest();
	testMe.testHeuristic();
	
}*/


public void testHeuristic() {
	
	int player1 = 1;
	int player2 = 2;
	int empty = 0;
	
	int test[][] = new int[7][6];

	test[0][5] = player1;	
//	test[1][5] = player2;
	test[2][5] = player1;
//	test[3][5] = player2;
	test[4][5] = player1;
	test[5][5] = player2;
	test[6][5] = player1;
	
	test[0][4] = player1;	
	test[1][4] = player1;
	test[2][4] = player1;
	test[3][4] = player2;
	test[4][4] = player2;
	test[5][4] = empty;
	test[6][4] = empty;
	
	test[0][3] = player1;	
	test[1][3] = player2;
	test[2][3] = player1;
	test[3][3] = player2;
	test[4][3] = empty;
	test[5][3] = empty;
	test[6][3] = empty;
	
	test[0][2] = empty;	
	test[1][2] = empty;
	test[2][2] = empty;
	test[3][2] = empty;
	test[4][2] = empty;
	test[5][2] = empty;
	test[1][2] = empty;	
	
	test[1][5] = player2;
	test[3][5] = player2;	
	
	int[] free = {2,2,2,2,3,4,4};
	
	printArrayRepresentation(test);
	
//	printBits(getBitRepresentation(test, player1));
//	printBits(getBitRepresentation(test, player2));
//	printBitRepresentation(getBitRepresentation(test, 0      ), test.length, test[0].length);
//	
	
	long emptyBoard = 279258638311359l;
			
	//0111111011111101111110111111011111101111110111111		
			
	
	
	int counts = 1;
	// ###################################################
		int testSlot = 2, testRow = 2;           //#######
	// ###################################################
		System.out.println("testSlot: " + testSlot + " testRow: " + testRow);
	System.out.print("elapsed time for " + counts +" computations: ");
	
	long lasttime = System.currentTimeMillis();
	
	Long boardforPlayer1 = getBitRepresentation(test, player1);
	Long boardforPlayer2 = getBitRepresentation(test, player2);

	long owned = 0l;
/*	
	for(;counts > 0;counts--){
		owned = (getRelevantDescendDiagonal(boardforPlayer1  , testSlot , testRow ));
		printBits(owned);
	
		owned = (getRelevantAscendDiagonal( boardforPlayer1 , testSlot , testRow ));
		printBits(owned);
		
		owned = (getRelevantSlot( boardforPlayer1 , testSlot, testRow));
		printBits(owned);
		
		owned = (getRelevantRow( boardforPlayer1 , testSlot, testRow ));
		printBits(owned);
	}
*/	
	
/*	
	int i = 0;
	for(;counts > 0;counts--){
		i = getHeuristicValue((getRelevantDescendDiagonal((boardforPlayer2^emptyBoard), testSlot, testRow )), getRelevantDescendDiagonal(boardforPlayer1   , testSlot , testRow ));
	//System.out.println("possibileties in descendD: " + i);
	
	    i += getHeuristicValue((getRelevantAscendDiagonal ((boardforPlayer2^emptyBoard), testSlot, testRow )), getRelevantAscendDiagonal(boardforPlayer1   , testSlot , testRow ));
	//System.out.println("possibileties in ascendD: " + i);	
	
    	i += getHeuristicValue((getRelevantRow            ((boardforPlayer2^emptyBoard), testSlot, testRow )), getRelevantRow           (boardforPlayer1   , testSlot , testRow ));
    //System.out.println("possibileties in row: " + i);

    	i += getHeuristicValue((getRelevantSlot           ((boardforPlayer2^emptyBoard), testSlot, testRow )), getRelevantSlot          (boardforPlayer1   , testSlot , testRow ));
	//System.out.println("possibileties in slot: " + i);
	
	}
*/	
	
	
	
	int i = 0;
	for(int z = 0; z < free.length; z++){
		i = getNOutOfFourValue((getRelevantDescendDiagonal((boardforPlayer2^emptyBoard), z, free[z] )), getRelevantDescendDiagonal(boardforPlayer1   , z , free[z] ), 50, 6, 2);
	//System.out.println("possibileties in descendD: " + i);
	
	    i += getNOutOfFourValue((getRelevantAscendDiagonal ((boardforPlayer2^emptyBoard), z, free[z] )), getRelevantAscendDiagonal(boardforPlayer1   , z , free[z] ), 50, 6, 2);
	//System.out.println("possibileties in ascendD: " + i);	
	
    	i += getNOutOfFourValue((getRelevantRow            ((boardforPlayer2^emptyBoard), z, free[z] )), getRelevantRow           (boardforPlayer1   , z , free[z] ), 50, 6, 2);
    //System.out.println("possibileties in row: " + i);

    	i += getNOutOfFourValue((getRelevantSlot           ((boardforPlayer2^emptyBoard), z, free[z] )), getRelevantSlot          (boardforPlayer1   , z , free[z] ), 50, 6, 2);
	//System.out.println("possibileties in slot: " + i);

    	System.out.println("Total value for ("+z+","+free[z]+") is "+i);
    	
	}	

	Config config = new Config();

	Logic logic = new Logic(config);
	
	BlockHeuristic h = new BlockHeuristic(4);
	logic.Board b = new logic.Board(7, 6);
	
	//System.out.println("       " + h.getBestColumn(b, 1));
	
	int result = 0;
	
	for(;counts > 0;counts--)	
		//h.getBestColumn(b, 1);
		result = getBestSlot(test,free,1,2,2);
	
	System.out.println((System.currentTimeMillis() - lasttime) + " ms");
	System.out.println("best row: "+ result);
	
	for(int v = 0; v < free.length; v++)
		System.out.println("heuristic value for slot " + v + ": " + getSlotValue(test,v,free[v],1,2,2));
	
	printArrayRepresentation(test);
	
	//System.out.println("best position: " + result);
	//System.out.println(haswon(getBitRepresentation(test, player2)));
	//printBitRepresentation(possible,test.length, test[0].length);
}




 
//BIT PACKING - currently not in use |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\

public void runPackingTest(){
	/*
	 *   11 -> 110000				(3 << 4)
	 *   11 -> 001100				(3 << 2)
	 *   11 -> 000011				 3
	 *      
	 *   (3 << 4) | (3 << 2) | 3  => 111111
	 *   
	 *   111111 >>> 4 			= 000011
	 *   111111 >>> 2             001111
	 *            & 3             000011  = 000011
	 *   111111 				  111111	
	 *   		  & 0x03	    = 000011  = 000011 
	 */
	
	byte packed = ( (3 << 4) | (2 << 2) | 1);
	System.out.println(  (packed >>> 4) + " - "+
					     (packed >>> 2 & 3) + " - " +
					     (packed & 0x03) + " (3 - 2 - 1) "); 
	packed =     ( (2 << 4) | (1 << 2) | 0);
	System.out.println(  (packed >>> 4) + " - "+
					     (packed >>> 2 & 3) + " - " +
					     (packed & 0x03) + " (2 - 1 - 0) "); 
	
	System.out.println(computeScore(packed) + " (7) "); // should be 7		
}

/*
 * _nOutOfFours should look like this:
 * 
 * how to pack:
 *  n2 << 4) | n3 << 2  | n4
 *
 * how to unpack:
 *  _packet_n2_n3_n4 >>> 4       =  total number of 2 out of four => n2 (each will give 1  point) 
 * (_packet_n2_n3_n4 >>> 2) & 3  =  total number of 3 out of four => n3 (each will give 5  point) 
 *  _packet_n2_n3_n4 & 0x03   	 =  total number of 4 out of four => n4 (each will give 50 point) 
 */
private int computeScore(int _packet_n2_n3_n4){
	return ((_packet_n2_n3_n4 >>> 4))*1 + ((_packet_n2_n3_n4 >>> 2) & 3)*5 + (_packet_n2_n3_n4 & 0x03)*50;
}	



//VISUALISATION METHODS |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\

private void printBits(long _possible) {
	//System.out.println(_possible);
	StringBuffer buf = new StringBuffer();
	
	for(short s = 0; s < 49; s++){
		buf.insert(0, _possible&1);
	    _possible>>>=1;
	}
	//System.out.println(buf);
}

private long getBitRepresentation(int[][] _board, int playerID) {
	long l = 0l;
	
	int slotCount = _board.length;
	int rowCount = _board[0].length;
	
	for(short slot = 0; slot < slotCount; slot++){
		l<<=1; // head	
		for(short row = 0; row < rowCount; row++ ){
			if(_board[slot][row] == playerID) ++l; // set last bit on 1
			l<<=1; // shift
		}			
	}
	
	l>>>=1; // remove last shift
	
	return l;
}


private void printArrayRepresentation(int [][] _board){

	 StringBuffer buf = new StringBuffer();
	
	int slotCount = _board.length;
	int rowCount = _board[0].length;
	
	for(short row = 0; row < rowCount; row++ ){
		for(short slot = 0; slot < slotCount; slot++)		
			buf.append(_board[slot][row]);
		buf.append("\n");	
	}
	
	//System.out.println(buf);
	
}




}





//EXPLANATIONS |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\



/*
##################### 
#     BITBOARD      #
#####################

1 2 3 4 5 6 7
. . . . . . .
. . . . . . .
@ . . . . . .
0 @ . . . . .
@ 0 @ . . . .
0 @ 0 @ . . 0

2130570 =  1000001000001010001010
33290	=  0000001000001000001010
33290	=  0000001000001000001010
8		=                    1000
8		=                    1000


2130570 =>

0 0 0 0 0 0 0
0 0 0 0 0 0 0
0 0 0 0 0 0 0 
1 0 0 0 0 0 0
0 1 0 0 0 0 0
1 0 1 0 0 0 0
0 1 0 1 0 0 0

33290 =>   

0 0 0 0 0 0 0    (=> eins nach links + eins nach oben -> �bereinstimmung �berpr�fen mit &)
0 0 0 0 0 0 0
0 0 0 0 0 0 0
1 0 0 0 0 0 0
0 1 0 0 0 0 0
1 0 1 0 0 0 0
0 0 0 0 0 0 0

y =>

0 0 0 0 0 0 0    (& ergibt selbes bitmuster)
0 0 0 0 0 0 0
0 0 0 0 0 0 0
1 0 0 0 0 0 0
0 1 0 0 0 0 0
1 0 1 0 0 0 0
0 0 0 0 0 0 0

8 =>

0 0 0 0 0 0 0    (2* >> 2*HEIGHT gives u the fourth coins from four in a row)
0 0 0 0 0 0 0
0 0 0 0 0 0 0
1 0 0 0 0 0 0
0 0 0 0 0 0 0
0 0 0 0 0 0 0
0 0 0 0 0 0 0



###################
#  FLIP BITBOARD  #
###################



/*

0000001
0000000
0001000
1212000
1112200
1010121

for player 1:

= 30856940896289
=> 0000111000001000001110001000000000100000000100001
=>

-> Top
|
0000111   first slot
0000010
0000111
0001000
0000001
0000000
0100001   last slot
      |
      -> Bottom

flip board:

=> 0100001000000000000010001000000011100000100000111
=>
0100001
0000000
0000001
0001000
0000111
0000010
0000111

*/

