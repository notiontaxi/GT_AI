/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import main.Config;

/**
 *
 * @author Alex
 */
public class Logic {
	private Player[] players;
	private int moveCount;
	private Config config;
	private int winnerID;
	private Board board;
	private int activePlayerID;
	private int[] lineArrayX;
	private int[] lineArrayY;
	private int dimXdimY;
	
	public Logic(Config config){
		this.config = config;
		players = new Player[2];
		players[0] = new Player(0,"Max");
		players[1] = new Player(1,"Min");
		
		winnerID = -1;
		activePlayerID = 0;
		dimXdimY = config.getDimensionX() * config.getDimensionY();
		board = new Board(config.getDimensionX(), config.getDimensionY());
		initLineArray();
	}	
	
	public boolean unsafePerformMove(int x, int y) {
		return performMove(x, y, false);
	}
	
	public boolean performMove(int x, int y) {
		return performMove(x, y, true);	
	}
	
	private boolean performMove(int x, int y, boolean useSafeMode) {
		if (useSafeMode){
			try {
				board.performMove(activePlayerID, x,y);
			} catch (IllegalAccessException e) {
				System.out.println("Invalid move(" + x + "," + y + ").");
				return false;
			}
		} else {
			board.unsafePerformMove(activePlayerID, x,y);
		}
		moveCount++;
		if (moveCount >= (2 * config.getRowLengthToWin()) - 1){
			
			long lasttime = System.currentTimeMillis();
			int counts = 1;
			
			for(;counts > 0;counts--){
				//if(calculateWinner(x,y))
				//	winnerID = activePlayerID;
				if(haswon(board.getFields(), activePlayerID))
					winnerID = activePlayerID;
				
			}
			
			//System.out.println("elapsed time in ms: " + (System.currentTimeMillis() - lasttime));
			
		}
		
		switchPlayer();
		
		return true;
	}

	public void undoMove(int x) {
		board.undoMove(x);
		this.winnerID = -1;
		moveCount--;
		switchPlayer();
	}

	public boolean isMovePossible(int x, int y) {
		return board.isFieldEmpty(x, y);
	}
	
	public boolean isGameOver(){
		return (moveCount == dimXdimY) || (winnerID != -1);
	}
	
	private void switchPlayer(){
		activePlayerID = (activePlayerID + 1) % 2;
	}

	public Player getWinner() {
		if (winnerID != -1)
			return players[winnerID];
		else
			return null;
	}
	
	public int getWinnerID() {
		return winnerID;
	}

	public int getInactivePlayer() {
		return (activePlayerID + 1) % 2;
	}
	
	public int getActivePlayer() {
		return activePlayerID;
	}
	
	public int getPlayerCount(){
		return 2;
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public int[][] getBoardFields(){
		return this.board.getFields();
	}
	
	private boolean calculateWinner(int x, int y){
		int posInArray = 0;
		int fieldsInRow = 1;
		int directionSwitches = 0;
		int xNew, xCurr, xCurrDelta;
		int yNew, yCurr, yCurrDelta;
		while (winnerID == -1 && posInArray <= 3){
			fieldsInRow = 1;
			xCurr = x;
			yCurr = y; 
			xCurrDelta = lineArrayX[posInArray];
			yCurrDelta = lineArrayY[posInArray];
			directionSwitches = 0;
			
			while(winnerID == -1 && directionSwitches < 2){
				xNew = xCurr + xCurrDelta;
				yNew = yCurr + yCurrDelta;
				if (board.isCoordinateValid(xNew, yNew) && board.unsafeGetFieldValue(xNew,yNew) == activePlayerID){
					fieldsInRow++;
					xCurr = xNew;
					yCurr = yNew;
				} else {
					xCurrDelta *= (-1);
					yCurrDelta *= (-1);
					directionSwitches++;
					xCurr = x;
					yCurr = y;
				}
				if (fieldsInRow == config.getRowLengthToWin()){
					return  true;
				}
			}
			posInArray++;
		}
		return false;
	}
	
/*
 * alternative	
 */
	final boolean haswon(int[][] _board, int playerID)
	{
		
		 long newboard = getBitboardRepresentation(_board, playerID);	
			
		 long y = newboard & (newboard>>Config.dimensionY);
		 if ((y & (y >> 2*config.dimensionY)) != 0){ // check diagonal \
			 return true;
		 }
		 y = newboard & (newboard>>(Config.dimensionY+1));
		 if ((y & (y >> 2*(Config.dimensionY+1))) != 0){ // check horizontal -
			 return true;
		 }
		 y = newboard & (newboard>>(Config.dimensionY+2)); // check diagonal /
		 if ((y & (y >> 2*(Config.dimensionY+2))) != 0){
			 return true;
		 }
		 y = newboard & (newboard>>1); // check vertical |
		 
		 
		 return (y & (y >> 2)) != 0;
	}


	private long getBitboardRepresentation(int[][] _board, int playerID) {
		
		long l = 0l;
		
		/*
		int slotCount = _board.length;
		int rowCount = _board[0].length;
		short slot = 0;
		short row = 0;
		
		
		for(; slot < slotCount; slot++){
			l<<=1; // head	
			for(; row < rowCount; row++ ){
				if(_board[slot][row] == playerID) l^=1; // set last bit on 1
				l<<=1; // shift
			}
			row = 0;				
		}
		
		return (l>>>=1); // remove last shift
		*/
		
		
		// UGLY!!! but faster! 0o
		
		// first slot
		if(_board[0][0] == playerID) l^=1;
		l<<=1; // shift
		if(_board[0][1] == playerID) l^=1;
		l<<=1; // shift
		if(_board[0][2] == playerID) l^=1;
		l<<=1; // shift
		if(_board[0][3] == playerID) l^=1;
		l<<=1; // shift
		if(_board[0][4] == playerID) l^=1;
		l<<=1; // shift		
		if(_board[0][5] == playerID) l^=1;
		l<<=2; // shift
		
		
		// first row
		if(_board[1][0] == playerID) l^=1;
		l<<=1; // shift
		if(_board[1][1] == playerID) l^=1;
		l<<=1; // shift
		if(_board[1][2] == playerID) l^=1;
		l<<=1; // shift
		if(_board[1][3] == playerID) l^=1;
		l<<=1; // shift
		if(_board[1][4] == playerID) l^=1;
		l<<=1; // shift		
		if(_board[1][5] == playerID) l^=1;
		l<<=2; // shift

		// first row
		if(_board[2][0] == playerID) l^=1;
		l<<=1; // shift
		if(_board[2][1] == playerID) l^=1;
		l<<=1; // shift
		if(_board[2][2] == playerID) l^=1;
		l<<=1; // shift
		if(_board[2][3] == playerID) l^=1;
		l<<=1; // shift
		if(_board[2][4] == playerID) l^=1;
		l<<=1; // shift		
		if(_board[2][5] == playerID) l^=1;
		l<<=2; // shift
		
		// first row
		if(_board[3][0] == playerID) l^=1;
		l<<=1; // shift
		if(_board[3][1] == playerID) l^=1;
		l<<=1; // shift
		if(_board[3][2] == playerID) l^=1;
		l<<=1; // shift
		if(_board[3][3] == playerID) l^=1;
		l<<=1; // shift
		if(_board[3][4] == playerID) l^=1;
		l<<=1; // shift		
		if(_board[3][5] == playerID) l^=1;
		l<<=2; // shift
	
		// first row
		if(_board[4][0] == playerID) l^=1;
		l<<=1; // shift
		if(_board[4][1] == playerID) l^=1;
		l<<=1; // shift
		if(_board[4][2] == playerID) l^=1;
		l<<=1; // shift
		if(_board[4][3] == playerID) l^=1;
		l<<=1; // shift
		if(_board[4][4] == playerID) l^=1;
		l<<=1; // shift		
		if(_board[4][5] == playerID) l^=1;
		l<<=2; // shift
		
		// first row
		if(_board[5][0] == playerID) l^=1;
		l<<=1; // shift
		if(_board[5][1] == playerID) l^=1;
		l<<=1; // shift
		if(_board[5][2] == playerID) l^=1;
		l<<=1; // shift
		if(_board[5][3] == playerID) l^=1;
		l<<=1; // shift
		if(_board[5][4] == playerID) l^=1;
		l<<=1; // shift		
		if(_board[5][5] == playerID) l^=1;
		l<<=2; // shift

		// first row
		if(_board[6][0] == playerID) l^=1;
		l<<=1; // shift
		if(_board[6][1] == playerID) l^=1;
		l<<=1; // shift
		if(_board[6][2] == playerID) l^=1;
		l<<=1; // shift
		if(_board[6][3] == playerID) l^=1;
		l<<=1; // shift
		if(_board[6][4] == playerID) l^=1;
		l<<=1; // shift		
		if(_board[6][5] == playerID) l^=1;
		l<<=2; // shift

		
		return l; // remove last shift
		
	}	
	
	
	
	
	private void initLineArray(){
		lineArrayX = new int[4];
		lineArrayX[0] = 1;
		lineArrayX[1] = 0;
		lineArrayX[2] = 1;
		lineArrayX[3] = -1;
		
		lineArrayY = new int[4];
		lineArrayY[0] = 0;
		lineArrayY[1] = 1;
		lineArrayY[2] = 1;
		lineArrayY[3] = 1;
	}
	
	@Override
	public Logic clone() {
		
		Logic logic = new Logic(config);

		logic.players = players.clone();
		logic.moveCount = this.moveCount;
		logic.config = new Config();
		logic.winnerID = this.winnerID;
		
		logic.board = this.board.clone();
		logic.activePlayerID = this.activePlayerID;
		logic.lineArrayX = this.lineArrayX.clone();
		logic.lineArrayY = this.lineArrayY.clone();
		
		return logic;
	}	

	public Config getConfig() {
		return config;
	}	
}
