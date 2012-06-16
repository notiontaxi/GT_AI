package heuristic;
import logic.Board;
import main.Config;


//bitmask looks in 7x6 case as follows:
//.  .  .  .  .  .  .  TOP (in empty board filled with 1)
//47 40 33 26 19 12 05
//46 39 32 25 18 11 04
//45 38 31 24 17 10 03
//44 37 30 23 16 09 02
//43 36 29 22 15 08 01
//42 35 28 21 14 07 00  BOTTOM


public class BoardHeuristic{
	
	private final static int GET_VALUE = 0;
	private final static int GET_SLOT = 1;
	private final static int GET_TOTAL_VALUE = 2;
	
	private final static int NoMATCHE     = 1;
	private final static int OneMATCH     = 1;
	private final static int TwoMATCHES   = 1;
	private final static int ThreeMATCHES = 1;
	

	
	private final static long EMPTYBOARD = 279258638311359l;
										   //0111111011111101111110111111011111101111110111111
	public BoardHeuristic(){
		
		
		
	}
	
	

	/**
	 * 
	 * @param board 
	 * @param playerID has to be 0 or 1
	 * @return The best column (0-6)
	 */
	public int getBestColumn(logic.Board board, int playerID) {
		return getBoardUtility_dependingOnFreePositions(board.getFields(), board.getTopFields(), playerID, GET_SLOT, true);
	}
	
	/**
	 * 
	 * @param board 
	 * @param playerID hat to be 0 or 1
	 * @return The value of the best column
	 */
	public int getBestColumnValue(logic.Board board, int playerID) {
		return getBoardUtility_dependingOnFreePositions(board.getFields(), board.getTopFields(), playerID, GET_VALUE, true);
	}

	/**
	 * 
	 * @param board 
	 * @param playerID hat to be 0 or 1
	 * @return The value of the best column
	 */
	public int getBoardUtility_2(logic.Board board, int playerID) {
		return getBoardUtility_dependingOnFreePositions(board.getFields(), board.getTopFields(), playerID, GET_TOTAL_VALUE, false);
	}	
	
	/**
	 * This Method returns the utility for the whole board. Utility includes
	 * positive value for n out of four
	 * positive value for winning lines
	 * negative value for n out of four for the opponent
	 * 
	 * @param board
	 * @param playerID has to be 0 or 1
	 * @return The ulility for the whole baord (could be NEGATIVE)
	 */
	public int getBoardUtility(logic.Board board, int playerID){
		return getBoardUtility(board.getFields(),  playerID);
	}


	
	private int getBoardUtility(int[][] _board, int activePlayer){
		
		Long boardPlayer1 = 0l;   
		Long boardPlayer2 = 0l;	

		boardPlayer1 = getBitRepresentation(_board, activePlayer);   // 1 sec
		boardPlayer2 = getBitRepresentation(_board, activePlayer^1);	
		int value = 0;
		
		for(int i = 0; i < 6 ; i++){
			value += getRowValue(boardPlayer1, boardPlayer2, i);
			value += getSlotValue(boardPlayer1, boardPlayer2, i);
			value += getDescendDiagonalValue(flipHorizantal(boardPlayer1), flipHorizantal(boardPlayer2), 5-i, 2);
			value += getDescendDiagonalValue(boardPlayer1, boardPlayer2, i, 2);
		}
		
		return (value +  getSlotValue(boardPlayer1, boardPlayer2, 6));
	}
	
	
	private int getBoardUtility_dependingOnFreePositions(int[][] _board, int freeRows[], int activePlayer, int resType, boolean active){			
		
		Long boardforPlayer1 = 0l;   // 1 sec
		Long boardforPlayer2 = 0l;	
		
		boardforPlayer1 = getBitRepresentation(_board, activePlayer);   // 1 sec
		boardforPlayer2 = getBitRepresentation(_board, activePlayer^1);	

		int value  = 0, bestSlot = 0, highestValue = -1, totalValue = 0, returnMe = 0; 
		
		
		for(int z = 0; z < 7; z++){
			value = getSlotValue(boardforPlayer1, boardforPlayer2, z, freeRows[z],activePlayer, active);//getPositionValue(boardforPlayer1, boardforPlayer2, z, freeRows[z]+1);
			totalValue += value; 
	    	if(value > highestValue){
	    		highestValue = value;
	    		bestSlot = z; 
	    	}
	    	value  = 0;
		}
		
		
		switch(resType){
		case GET_SLOT:
			returnMe = bestSlot;
			break;
		case GET_VALUE:
			returnMe = highestValue;
			break;
		case GET_TOTAL_VALUE:
			returnMe = totalValue;
			break;
		}
		
		
		return returnMe;
}



	
	private int getDescendDiagonalValue(long boardPlayer1, long boardPlayer2,  int slot, int row){
		int scoreValue = 0, blockValue  = 0, linesValue = 0;
		int freeDescendDiagonalCodeP1 = getDescendDiagonal((boardPlayer2^EMPTYBOARD), slot, row );
		int freeDescendDiagonalCodeP2 = getDescendDiagonal((boardPlayer1^EMPTYBOARD), slot, row );
	
		linesValue =  getNumberOfWinningLines(freeDescendDiagonalCodeP1); 
		scoreValue =  getNOutOfFourValue(     freeDescendDiagonalCodeP1, getDescendDiagonal(boardPlayer1 , slot , row ), 100, 50, 10, 5); 
		blockValue =  getNOutOfFourValue(     freeDescendDiagonalCodeP2, getDescendDiagonal(boardPlayer2 , slot , row ), 100, 50, 10, 2);  
		
		return linesValue + scoreValue - blockValue;
	}	
	
	
	private int getRowValue(long boardPlayer1, long boardPlayer2, int row){
		int scoreValue = 0, blockValue  = 0, linesValue = 0;
		int freeRowCodeP1 			= getRow            ((boardPlayer2^EMPTYBOARD), row );
		int freeRowCodeP2 			= getRow            ((boardPlayer1^EMPTYBOARD), row );
		
		linesValue += getNumberOfWinningLines(freeRowCodeP1);  
		scoreValue += getNOutOfFourValue(     freeRowCodeP1, getRow(boardPlayer1 , row ), 100, 50, 10, 5);
		blockValue += getNOutOfFourValue(     freeRowCodeP2, getRow(boardPlayer2 , row ), 100, 50, 10, 2);
		
		return linesValue + scoreValue - blockValue;
	}
	
	private int getSlotValue(long boardPlayer1, long boardPlayer2, int slot){
		int scoreValue = 0, blockValue  = 0, linesValue = 0;
		int freeSlotCodeP1 			= getSlot           ((boardPlayer2^EMPTYBOARD), slot);	
		int freeSlotCodeP2 			= getSlot           ((boardPlayer1^EMPTYBOARD), slot);	
		
		linesValue += getNumberOfWinningLines(freeSlotCodeP1); 
		scoreValue += getNOutOfFourValue(     freeSlotCodeP1, 	getSlot(boardPlayer1 , slot ), 100, 50, 10, 5);  
		blockValue += getNOutOfFourValue(     freeSlotCodeP2, 	getSlot(boardPlayer2 , slot ), 100, 50, 10, 2);

		return linesValue + scoreValue - blockValue;
	}
	

	/**
	 * This Method returns the utility for the specified position
	 * for player 1
	 * 
	 * @param _boardPlayer1
	 * @param _boardPLayer2
	 * @param slot
	 * @param row
	 * @return Utility for the specified position (could be NEGATIVE)
	 */
private int getSlotValue(long boardPlayer1, long boardPlayer2, int slot, int row, int activePlayer, boolean active){
	
	
	long emptyBoardPlayer1 = boardPlayer2^EMPTYBOARD, emptyBoardPlayer2 = boardPlayer1^EMPTYBOARD;
	int scoreValue = 0, blockValue  = 0, linesValue = 0;
	

	
	int freeDescendDiagonalCode = getRelevantDescendDiagonal((emptyBoardPlayer1), slot, row );
	int freeAscendDiagonalCode  = getRelevantDescendDiagonal ((flipHorizantal(emptyBoardPlayer1)), 5-slot, row ); 
	int freeRowCode 			= getRelevantRow            ((emptyBoardPlayer1), slot, row );
	int freeSlotCode 			= getRelevantSlot           ((emptyBoardPlayer1), slot, row );	
	
	
	scoreValue =  getNOutOfFourValue(freeDescendDiagonalCode, getRelevantDescendDiagonal(boardPlayer1 , slot , row ), 0, 100, 10, 5);  
	scoreValue += getNOutOfFourValue(freeAscendDiagonalCode,  getRelevantDescendDiagonal (flipHorizantal(boardPlayer1) , 5-slot , row ), 0, 100, 10, 5);  
	scoreValue += getNOutOfFourValue(freeRowCode, 			  getRelevantRow            (boardPlayer1 , slot , row ), 0, 100, 10, 5);  
	scoreValue += getNOutOfFourValue(freeSlotCode, 			  getRelevantSlot           (boardPlayer1 , slot , row ), 0, 100, 10, 5);  

	linesValue =  getNumberOfWinningLines(freeDescendDiagonalCode); 
	linesValue += getNumberOfWinningLines(freeAscendDiagonalCode);  
	linesValue += getNumberOfWinningLines(freeRowCode);  
	linesValue += getNumberOfWinningLines(freeSlotCode);  

	
	blockValue =  getNOutOfFourValue((getRelevantDescendDiagonal((emptyBoardPlayer2), slot, row )), getRelevantDescendDiagonal(boardPlayer2 , slot , row ), 0, 100, 10,3);  // 1.8s    avoid killer-moves
	blockValue += getNOutOfFourValue((getRelevantDescendDiagonal((flipHorizantal(emptyBoardPlayer2)), 5-slot, row )), getRelevantDescendDiagonal (flipHorizantal(boardPlayer2) , 5-slot , row ), 0, 100, 10,3);  // 2.4s
	blockValue += getNOutOfFourValue((getRelevantRow            ((emptyBoardPlayer2), slot, row )), getRelevantRow            (boardPlayer2 , slot , row ), 0, 100, 10,3);  // 2.4s
	blockValue += getNOutOfFourValue((getRelevantSlot           ((emptyBoardPlayer2), slot, row )), getRelevantSlot           (boardPlayer2 , slot , row ), 0, 100, 10, 3);  // 1.3s

	if(active)
		return scoreValue + blockValue + linesValue;
	else
		return scoreValue - blockValue + linesValue;
}





private int getNOutOfFourValue(long possible, long owned, int valFor4Hits, int valFor3Hits, int valFor2Hits, int valFor1Hit) { //2.5s
	long thisChance = 0;
	int result = 0;
	
// nice version	
/*	
	for(int i = 0; i < 4; i++){
		if((possible&15) == 15){
			thisChance = 0;
			for(int j = 0; j<4;j++)
				thisChance += (owned>>j)&1;
			
			if(thisChance == 0);
			else if(thisChance == 1) result += valFor1Hit;
			else if(thisChance == 2) result += valFor2Hits;
			else if(thisChance == 3) result += valFor3Hits;
			else if(thisChance == 3) result += valFor4Hits;
			
		}
		possible>>>=1;
		owned>>>=1;
	}
*/	

	
// fast version
	if((possible&15) == 15){
		thisChance = ((owned&1) + ((owned>>1)&1) + ((owned>>2)&1) + ((owned>>3)&1));
		result += (thisChance == 0) ? 0 : ((thisChance == 1) ? valFor1Hit : ((thisChance == 2) ? valFor2Hits : ((thisChance == 3) ? valFor3Hits : valFor4Hits)));
	}
	possible>>>=1; owned>>>=1;
	if((possible&15) == 15){
		thisChance = ((owned&1) + ((owned>>1)&1) + ((owned>>2)&1) + ((owned>>3)&1));
		result += (thisChance == 0) ? 0 : ((thisChance == 1) ? valFor1Hit : ((thisChance == 2) ? valFor2Hits : ((thisChance == 3) ? valFor3Hits : valFor4Hits)));
	}
	possible>>>=1; owned>>>=1;
	if((possible&15) == 15){
		thisChance = ((owned&1) + ((owned>>1)&1) + ((owned>>2)&1) + ((owned>>3)&1));
		result += (thisChance == 0) ? 0 : ((thisChance == 1) ? valFor1Hit : ((thisChance == 2) ? valFor2Hits : ((thisChance == 3) ? valFor3Hits : valFor4Hits)));
	}
	possible>>>=1; owned>>>=1;	
	if((possible&15) == 15){
		thisChance = ((owned&1) + ((owned>>1)&1) + ((owned>>2)&1) + ((owned>>3)&1));
		result += (thisChance == 0) ? 0 : ((thisChance == 1) ? valFor1Hit : ((thisChance == 2) ? valFor2Hits : ((thisChance == 3) ? valFor3Hits : valFor4Hits)));
	}
	possible>>>=1; owned>>>=1;	
	
	
	return result;
}

private int getNumberOfWinningLines(long possible) { //2.5s
	int result = 0;
	
// nice version	
/*	for(int i = 0; i < 4; i++){
		if((possible&15) == 15){
			result++;
		}
		possible>>>=1;
	}
*/
// fast version	
	if((possible&15) == 15) result++; possible>>>=1;	
	if((possible&15) == 15) result++; possible>>>=1;
	if((possible&15) == 15) result++; possible>>>=1;
	if((possible&15) == 15) result++; possible>>>=1;

	return result;
}



/*
0 : 0-6    : lnew = l>>>(height+1*)5 & 127 (111111)  
1 : 7-8
*/
private int getRelevantSlot(long _board, int slot, int row){
	int l =  getSlot(_board, slot);
	
	l<<=1;

	if(row<3)
		l>>=3-row;
	else
		l<<=row-3;	
	
	
	return l;
}


private int getSlot(long _board, int slot) {
	return (int)((_board>>>((Config.dimensionY+1)*(6-slot))) & 127);
}



private int getRelevantRow(long _board, int _slot, int _row){
	
	int row = getRow(_board, _row);
		
	if(_slot<3)
		row<<=3-_slot;
	else
		row>>=_slot-3;	
	
	return row&127; // 1111111
}

private int getRow(long _board, int row) {
	long l = _board>>>=((Config.dimensionY-1)-row); // push bit with wanted row to position 0
	int result = 0;
	for(int i = 0; i < Config.dimensionX; i++){
		result<<=1;
		result += (l>>>(i*(Config.dimensionY+1))) & 1; // push one row & get value
	}
	return result;
}



private int getRelevantDescendDiagonal(long _board, int slot, int row){
	
	int diagonal = getDescendDiagonal(_board, slot, row);
	
	if(row<2)
		diagonal<<=2-row;
	else
		diagonal>>=row-2;	
	
	return diagonal;
}

private int getDescendDiagonal(long _board, int slot, int row) {
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
	
	return result;
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





// TESTING |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\

private static void main(String[] args) {
	BoardHeuristic testMe = new BoardHeuristic();
//	testMe.runPackingTest();
	testMe.testHeuristic();
	
}


private void testHeuristic() {
	
	int player1 = 1;
	int player2 = 0;
	int empty   = 8;
	
	int test[][] = new int[7][6];

	test[0][5] = player1;	
	test[1][5] = player1;
	test[2][5] = player1;
	test[3][5] = player2;
	test[4][5] = player1;
	test[5][5] = player2;
	test[6][5] = player1;
	
	test[0][4] = player1;	
	test[1][4] = player1;
	test[2][4] = player1;
	test[3][4] = player1;
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
	test[6][2] = empty;	

	test[0][1] = empty;	
	test[1][1] = empty;
	test[2][1] = empty;
	test[3][1] = empty;
	test[4][1] = empty;
	test[5][1] = empty;
	test[6][1] = empty;		

	test[0][0] = empty;	
	test[1][0] = empty;
	test[2][0] = empty;
	test[3][0] = empty;
	test[4][0] = empty;
	test[5][0] = empty;
	test[6][0] = empty;	

	
	int[] free = {2,2,2,2,3,4,4};
	printArrayRepresentation(test);
	long boardForPlayer1 = getBitRepresentation(test, player1);
	
	//0111111011111101111110111111011111101111110111111		
			
	 printBoard(boardForPlayer1);
	 /*
	 System.out.println(sumOfBitsInBoard(boardForPlayer1));
	 long y = boardForPlayer1 & (boardForPlayer1>>(Config.dimensionY+2));
	 printBoard(y);
	 System.out.println(sumOfBitsInBoard(y));
	 	  y = y & (boardForPlayer1>>(Config.dimensionY+2)*2);
	 printBoard(y);
	 System.out.println(sumOfBitsInBoard(y));
		  y = y & (boardForPlayer1>>(Config.dimensionY+2) *3);
	 printBoard(y);
	 System.out.println(sumOfBitsInBoard(y));
	 */
	
	int counts = 1000000;
	// ###################################################
		int testSlot = 2, testRow = 2;           //#######
	// ###################################################
	
		
		System.out.println(getBoardUtility(test,1));	
		
/*		
	System.out.println("testSlot: " + testSlot + " testRow: " + testRow);
	System.out.print("elapsed time for " + counts +" computations: ");
	
	long lasttime = System.currentTimeMillis();
	int result = 0;
	
	for(;counts > 0;counts--)	
		result = getSlotValue(test,1,2,0,1,1);
	
	System.out.println((System.currentTimeMillis() - lasttime) + " ms");
	System.out.println("best value: "+ result);
*/	
//	for(int v = 0; v < free.length; v++)
//		System.out.println("heuristic value for slot " + v + ": " + getSlotValue(test,v,free[v],1,2,2));

}




 
private short sumOfBitsInBoard(long y) {
	short sum = 0;
	
	for(short s = 0; s < 49; s++)
		sum += y>>>s&1l;
	
	return sum;
}


//BIT PACKING - currently not in use |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\

private void runPackingTest(){
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
	System.out.println(buf);
}

private void printBoard(long _possible) {
	//printBits(_possible);
	StringBuffer buf = new StringBuffer();
	
	for(short i = 0; i < 6; i++){
		for(short s = 0; s < 7; s++){
			buf.insert(0, (" " + ((_possible>>s*7+i)&1)));
		}
		buf.insert(0,"\n");
	}
	System.out.println(buf);
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
	
	System.out.println(buf);
	
}







}





//EXPLANATIONS |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\



/*
##################### 
#     BITBOARD      #
#####################


-> WIN <-
 
0 0 0 0 0 0 0
0 0 0 0 0 0 0
0 0 0 0 0 0 0 
1 1 0 0 0 0 0
1 1 0 0 0 0 0
1 1 1 1 0 0 0
1 1 1 1 1 0 0




1 2 3 4 5 6 7
. . . . . . .
. . . . . . .
@ . . . . . .
0 @ . . . . .
@ 0 @ . . . .
444
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



//NOT IN USE |/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\
//|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\|/-\
/*
private boolean haswon(int[][] _board, int playerID)
{
	Long boardforPlayer1 = 0l;   // 1 sec
	Long boardforPlayer2 = 0l;	
	long emptyBoardForPlayer1 = 0l, emptyBoardForPlayer2 = 0l, emptyBoard = 279258638311359l;
															   //0111111011111101111110111111011111101111110111111
	
	boardforPlayer1 = getBitRepresentation(_board, playerID);   // 1 sec
	boardforPlayer2 = getBitRepresentation(_board, playerID^1);	
	emptyBoardForPlayer1 = boardforPlayer2^emptyBoard;
	emptyBoardForPlayer2 = boardforPlayer1^emptyBoard;
	
			
		 long y = boardforPlayer1 & (boardforPlayer1>>Config.dimensionY);
		 
		 if ((y & (y >> 2*Config.dimensionY)) != 0){ // check diagonal /
			 return true;
		 }
		 y = boardforPlayer1 & (boardforPlayer1>>(Config.dimensionY+1));
		 if ((y & (y >> 2*(Config.dimensionY+1))) != 0){ // check horizontal -
			 return true;
		 }
		 y = boardforPlayer1 & (boardforPlayer1>>(Config.dimensionY+2)); // check diagonal \
		 if ((y & (y >> 2*(Config.dimensionY+2))) != 0){
			 return true;
		 }
		 y = boardforPlayer1 & (boardforPlayer1>>1); // check vertical |
		 
		 
		 return (y & (y >> 2)) != 0;
	}	
*/