package logic;
import java.io.*;

import main.Config;


public class HeuristicNOutOfFour {
	
	
	MyBoard board;
	
	public HeuristicNOutOfFour(){
		
		
	}
	
	
	public int getBestSlot(byte[][] _b){
		return computeScore(findWinningLinesForSlot(_b,0));
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
	
	
	
	private int findWinningLinesForSlot(byte[][] _b, int slot) {

		byte nextFreePosition = _b[slot][6];		
		byte n2 = 0, n3 = 0, n4 = 0; 
		
		

		return (n2 << 4) | (n3 << 2) | n4;
	}	
	
	private int checkSum(int _val){
		if(_val <= 0)
			return 0;
		return _val;
	} 


	
// Example board representation	
	private class MyBoard{
		
		public static final int EMPTY = -5;
		public static final int COMP  =  1;
		public static final int HUMAN =  0;
		
		
		byte[][] fields;
		
		public MyBoard(){
			fields = new byte[7][6];
			initFields();
		}
		


		private void initFields(){
			for(int slot = 0; slot < fields.length; ++slot){
				for (int row = 0; row < fields[0].length; ++row){
					fields[slot][row] = EMPTY;
				}
			}
		}
		
	/*
	Testfield looks like this:
	______________
	| | | | | | | |
	| | | | | | | |
	| | |x| | | | |
	| | |o| | | | |
	| |x|o|o| | | |
	|x|o|o|x| |x| |
	---------------
	 */
		private void initTestField(){
			
			initFields();
			
			this.fields[0][5] = COMP;
			
			this.fields[1][4] = COMP;
			this.fields[1][5] = HUMAN;
			
			this.fields[2][2] = COMP;
			this.fields[2][3] = HUMAN;
			this.fields[2][4] = HUMAN;
			this.fields[2][5] = HUMAN;
			
			this.fields[3][4] = HUMAN;
			this.fields[3][5] = COMP;
			
			this.fields[5][5] = COMP;			
		}			
	}
	

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
	
	
	public static void main(String[] args) {
		HeuristicNOutOfFour testMe = new HeuristicNOutOfFour();
//		testMe.runPackingTest();
//		testMe.testConnect4();
		testMe.testArrayVSBit();
		
	}

	



private void testArrayVSBit() {
		Connect4 testMe = new Connect4();
		testMe.testRepresentations();
	}


private void testConnect4() {
		Connect4 testMe = new Connect4();
		testMe.test();
		
	}





private class Connect4 {
	
 long color[];  // black and white bitboard
 final int WIDTH = 7;
 final int HEIGHT = 6;
//bitmask corresponds to board as follows in 7x6 case:
//.  .  .  .  .  .  .  TOP
//5 12 19 26 33 40 47
//4 11 18 25 32 39 46
//3 10 17 24 31 38 45
//2  9 16 23 30 37 44
//1  8 15 22 29 36 43
//0  7 14 21 28 35 42  BOTTOM
 final int H1 = HEIGHT+1;
 final int H2 = HEIGHT+2;
 final int SIZE = HEIGHT*WIDTH;
 final int SIZE1 = H1*WIDTH;
 final long ALL1 = (1L<<SIZE1)-1L; // assumes SIZE1 < 63
 final int COL1 = (1<<H1)-1;
 final long BOTTOM = ALL1 / COL1; // has bits i*H1 set
 final long TOP = BOTTOM << HEIGHT;

int moves[],nplies;
byte nextFreeRowInColumn[]; // holds bit index of lowest free square 
			   // also information about where are no coins

public Connect4()
{
 color = new long[2];
 nextFreeRowInColumn = new byte[WIDTH];
 moves = new int[SIZE];
 reset();
}



void reset()
{
 nplies = 0;
 color[0] = color[1] = 0L;
 for (int i=0; i<WIDTH; i++)
   nextFreeRowInColumn[i] = (byte)(H1*i);
}




final boolean haswon(long newboard)
{
	
 long y = newboard & (newboard>>HEIGHT);
 if ((y & (y >> 2*HEIGHT)) != 0){ // check diagonal \
	 return true;
 }
 y = newboard & (newboard>>H1);
 if ((y & (y >> 2*H1)) != 0){ // check horizontal -
	 return true;
 }
 y = newboard & (newboard>>H2); // check diagonal /
 if ((y & (y >> 2*H2)) != 0){
	 return true;
 }
 y = newboard & (newboard>>1); // check vertical |
 
 
 return (y & (y >> 2)) != 0;
}




final boolean haswon(int[][] _board, int playerID)
{
	
 long newboard = getBitRepresentation(_board, playerID);	
	
 long y = newboard & (newboard>>HEIGHT);
 if ((y & (y >> 2*HEIGHT)) != 0){ // check diagonal \
	 return true;
 }
 y = newboard & (newboard>>H1);
 if ((y & (y >> 2*H1)) != 0){ // check horizontal -
	 return true;
 }
 y = newboard & (newboard>>H2); // check diagonal /
 if ((y & (y >> 2*H2)) != 0){
	 return true;
 }
 y = newboard & (newboard>>1); // check vertical |
 
 
 return (y & (y >> 2)) != 0;
}


public void testRepresentations() {
	
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
	test[3][2] = player1;
	test[4][2] = empty;
	test[5][2] = empty;
	test[6][2] = empty;	
	
//	test[0][0] = player2;
//	test[1][1] = player2;	
	

	
	printArrayRepresentation(test);
	
	printBits(getBitRepresentation(test, player1));
	printBits(getBitRepresentation(test, player2));
//	printBitRepresentation(getBitRepresentation(test, 0      ), test.length, test[0].length);
//	
	long possible = (getRelevantDescendDiagonal( getBitRepresentation(test, player2) , 0 , 0 ));
	printBits(possible);
	
	//System.out.println(haswon(getBitRepresentation(test, player2)));
	//printBitRepresentation(possible,test.length, test[0].length);
}


private void printBits(long _possible) {
	System.out.println(_possible);
	StringBuffer buf = new StringBuffer();
	
	for(short s = 0; s < 49; s++){
		buf.insert(0, _possible&1);
	    _possible>>>=1;
	}
	
	System.out.println(buf);
}


/*
0 : 0-6    : lnew = l>>>(height+1*)5 & 127 (111111)  
1 : 7-8
*/
private long getRelevantSlot(long _board, int slot){
	return (_board>>>((Config.dimensionY+1)*(6-slot))) & 127;
}


private long getRelevantRow(long _board, int row){
	
	long l = _board>>>=((Config.dimensionY-1)-row); // push bit with wanted row to position 0
	long result = (l & 1);
	
	for(int i = 0; i < Config.dimensionX; i++){
		result += (l>>>(i*(Config.dimensionY+1))) & 1; // push one row
		result<<=1;
	}
	
	return (result>>>1)&127; // 1111111
}

private long getRelevantDescendDiagonal(long _board, int slot, int row){
	
									//1000000
	long mask = 141289400074368l;	//0100000
									//0010000
									//0001000
									//0000100
									//0000010
	
			
	// shift scanned diagonal from the board -> first potential in left upper edge
	long l = _board<<((Config.dimensionY)*(slot));
	

	
	// last potential right lower edge
	l = l&mask;
	l = (l>>>Config.dimensionY+1);

	long result = 0l;
	
	for(int i = 0; i < Config.dimensionX-1; i++){
		result<<=1;
		result += (l>>>(i*(Config.dimensionY+2))) & 1; // push one row
		
	}
	
	return result&127; // 1111111
}

private long getRelevantAscendDiagonal(long _board, int slot, int row){
				
	// shift scanned diagonal from the board -> first potential in left upper edge
	long l = _board<<((Config.dimensionY)*(slot));
	// last potential right lower edge
	l = (l>>>Config.dimensionY+1);
	
	long result = l&1;
	for(int i = 0; i < Config.dimensionX-1; i++){
		result<<=1;
		result += (l>>>(i*(Config.dimensionY+2))) & 1; // push one row
		
	}
	
	return result&127; // 1111111
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

private void printBitRepresentation(long _board, int slotCount, int rowCount){
	
	 StringBuffer buf = new StringBuffer();
	 long currLong = _board;
	 String s = "";
	 
	// System.out.println(_board);
 	
	 for(short slot = 0; slot < slotCount; slot++)	
		 for(short row = 0; row < rowCount+1; row++ ){
			 s =  (currLong&1l) + s;
			 currLong >>>= 1;	
	}	
	
	System.out.println(s);
	
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





public String toString()
{
 StringBuffer buf = new StringBuffer();

 for (int i=0; i<nplies; i++)
   buf.append(1+moves[i]);
 buf.append("\n");
 for (int w=0; w<WIDTH; w++)
   buf.append(" "+(w+1));
 buf.append("\n");
 for (int h=HEIGHT-1; h>=0; h--) {
   for (int w=h; w<SIZE1; w+=H1) {
     long mask = 1L<<w;
     buf.append((color[0]&mask)!= 0 ? " @" :
                (color[1]&mask)!= 0 ? " 0" : " .");
   }
   buf.append("\n");
 }
 if (haswon(color[0]))
   buf.append("@ won\n");
 if (haswon(color[1]))
   buf.append("O won\n");
 

 
 return buf.toString();
}



// return whether newboard lacks overflowing column
final boolean islegal(long newboard)
{
 return (newboard & TOP) == 0;
}

// return whether newboard is legal and includes a win
final boolean islegalhaswon(long newboard)
{
 return islegal(newboard) && haswon(newboard);
}

// return whether newboard includes a win



public long positioncode()
{
 //return 2*color[0] + color[1] + BOTTOM;
	return color[0];
//color[0] + color[1] + BOTTOM forms bitmap of heights
//so that positioncode() is a complete board encoding
}

void makemove(int _column) 
{
 color[nplies&1] ^= 1L<<nextFreeRowInColumn[_column]++;
 moves[nplies++] = _column;
}

public void test()
{
 Connect4 c4;
 String line;
 int col=0, i, result;
 long nodes, msecs;

 c4 = new Connect4();
 c4.reset();
 BufferedReader dis = new BufferedReader(new InputStreamReader(System.in));

 for (;;) {
   System.out.println("position " + c4.positioncode() + " after moves " + c4 + "enter move(s):");
   try {
     line = dis.readLine();
   } catch (IOException e) {
     System.out.println(e);
     System.exit(0);
     return;
   }
   if (line == null)
     break;
   for (i=0; i < line.length(); i++) {
     col = line.charAt(i) - '1';
     if (col >= 0 && col < WIDTH && c4.isplayable(col))
       c4.makemove(col);
   }
 }
 }



//return whether columns col has room
final boolean isplayable(int col)
{
return islegal(color[nplies&1] | (1L << nextFreeRowInColumn[col]));
}

void backmove()
{
 int n;

 n = moves[--nplies];
 color[nplies&1] ^= 1L<<--nextFreeRowInColumn[n];
}
}






}





/*
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

0 0 0 0 0 0 0    (=> eins nach links + eins nach oben -> übereinstimmung überprüfen mit &)
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

 *
 *
-- ----------------------------

1 2 3 4 5 6 7
. . . . . . .
. . . . . . .
@ . . . . . .
0 @ . . . . .
@ 0 @ . . . .
0 @ 0 @ . . 0


0 0 0 0 0 0 0    to test
0 0 0 0 0 0 0
0 0 0 0 0 0 0 
0 0 0 1 0 0 0   <- this one is set
0 0 0 1 1 0 0   
0 0 0 0 0 0 0
0 0 0 1 0 0 0

0 0 0 0 0 0 0    opponend
0 0 0 0 0 0 0
0 0 0 0 0 0 0 
0 0 0 0 0 0 0
0 0 0 0 0 0 0
0 0 0 1 1 1 0
0 0 0 0 1 1 1

1 0 0 0 0 0 0    mask
0 1 0 0 0 0 0
0 0 1 0 0 0 0 
0 0 0 1 0 0 0
0 0 0 0 1 0 0
0 0 0 0 0 1 0
0 0 0 0 0 0 1

1 0 0 0 0 0 0     mask ^ opponend
0 1 0 0 0 0 0
0 0 1 0 0 0 0 
0 0 0 1 0 0 0
0 0 0 0 1 0 0
0 0 0 0 0 0 0
0 0 0 0 0 0 0



 *
 */
