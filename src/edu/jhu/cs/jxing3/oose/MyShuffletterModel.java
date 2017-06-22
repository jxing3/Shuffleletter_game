package edu.jhu.cs.jxing3.oose;

import java.io.*;
import java.util.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.jhu.cs.oose.fall2014.shuffletter.iface.Position;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterIllegalMoveEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModel;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterModelListener;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTile;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTileMovedEvent;
import edu.jhu.cs.oose.fall2014.shuffletter.iface.ShuffletterTilePlayedEvent;
import edu.jhu.cs.jxing3.oose.MyShuffletterTile;


/** An implementation of the ShuffletterModel interface
 * @author Jesse Xing
 *
 */
public class MyShuffletterModel implements ShuffletterModel{

	private String authorname;
	private List<ShuffletterModelListener> listeners;
	private Map<Position,ShuffletterTile> board;
	private Set<String> wordlist;
	private List<ShuffletterTile> bag;
	private List<ShuffletterTile> supply;
	private boolean gameover;

	
	
	/**Default constructor for a MyShuffletterModel object
	 * Places 144 character tiles in a bag with specified distribution
	 * Shuffles bag and randomly takes 21 tiles out and places them in the supply
	 * @throws IOException if dictionary file not in files/wordlist.txt
	 */
	public MyShuffletterModel() throws IOException {
		this.authorname = "Jesse Xing";
		this.listeners = new ArrayList<>();
		this.bag = new ArrayList<>();
		this.supply = new ArrayList<>();
		this.wordlist = new HashSet<>();
		this.board = new HashMap<>();
		this.gameover = false;
		
		Scanner fromFile = new Scanner(new FileReader("files" + File.separator + "wordlist.txt"));
		
		while(fromFile.hasNext()){
			
			this.wordlist.add(fromFile.nextLine().toLowerCase());
			
		}
		/*
		int[] numdistribution = {5,9,1,1,3,1,2,1,1,1,1};

		int[] distribution = {2,3,4,5,6,8,9,11,12,13,18};
				
		char[] characters = {'J','K','Q','X','Z','B','C','F','H','M','P','V','W','Y','G','L','D','S','U','N','T','R','O','I','A','E'};
		
		int index = 0;
		for (int i=0; i<11; i++) {
			for(int j=0; j<numdistribution[i]; j++) {
				for(int k=0; k<distribution[i]; k++) {
					this.bag.add(new MyShuffletterTile(characters[index],false));
				}
				index++;
			}
			
		}*/

		this.bag.add(new MyShuffletterTile('A',false));
		this.bag.add(new MyShuffletterTile('P',false));
		this.bag.add(new MyShuffletterTile('P',false));
		this.bag.add(new MyShuffletterTile('L',false));
		this.bag.add(new MyShuffletterTile('E',false));
		this.bag.add(new MyShuffletterTile('E',false));
		this.bag.add(new MyShuffletterTile('R',false));

		this.bag.add(new MyShuffletterTile(' ',true));
		
		for (int i=0; i<7; i++) {
			this.supply.add(this.bag.remove(0));
		}
		//this.bag.add(new MyShuffletterTile(' ',true));
		
		//Collections.shuffle(this.bag);
		
		/*for (int i=0; i<21; i++) {
			this.supply.add(this.bag.remove(0));
		}*/
		
		fromFile.close();
		
	}

	@Override
	public void addListener(ShuffletterModelListener arg0) {
		this.listeners.add(arg0);		
	}

	@Override
	public void endRound() {
		if(this.supply.isEmpty()&&checkorthogonal()&&checkwords()){
			if(this.bag.isEmpty()) {
				for(ShuffletterModelListener listener : this.listeners){
					listener.gameEnded();
				}
				this.gameover = true;
				return;
			}
			this.supply.add(this.bag.remove(0));
			for(ShuffletterModelListener listener : this.listeners){
				listener.roundEnded();
			}
			
		} else if(!this.supply.isEmpty()){
			for(ShuffletterModelListener listener: this.listeners){
				listener.illegalMoveMade(new ShuffletterIllegalMoveEvent("Supply is not empty"));
			}		
		} else if(!checkorthogonal()){
			for(ShuffletterModelListener listener: this.listeners){
				listener.illegalMoveMade(new ShuffletterIllegalMoveEvent("Tiles not connected orthogonally"));
			}
		} else if(!checkwords()) {
			for(ShuffletterModelListener listener: this.listeners){
				listener.illegalMoveMade(new ShuffletterIllegalMoveEvent("Some words are not spelled correctly"));
			}
		}
	}

	@Override
	public String getAuthorName() {
		return this.authorname;
	}

	@Override
	public int getBagCount() {
		return this.bag.size();
	}

	@Override
	public Set<String> getLegalWords() {
		return this.wordlist;
	}

	@Override
	public List<ShuffletterTile> getSupplyContents() {
		return this.supply;
	}

	@Override
	public ShuffletterTile getTile(Position arg0) {
		return this.board.get(arg0);
	}

	@Override
	public Collection<Position> getTilePositions() {
		return this.board.keySet();
	}

	@Override
	public boolean isGameOver() {
		return this.gameover;
	}

	@Override
	public void move(Position arg0, Position arg1) {
		
		if(this.gameover) {			
			return;
		}
		if(!board.containsKey(arg0)) {
			throw new IllegalArgumentException();
			
		} 
		
		if(board.containsKey(arg1)) {
			ShuffletterTile letter1 = this.board.get(arg0);
			ShuffletterTile letter2 = this.board.get(arg1);
			this.board.remove(arg0);
			this.board.remove(arg1);
			this.board.put(arg0, letter2);
			this.board.put(arg1, letter1);
			
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.tileMoved(new ShuffletterTileMovedEvent(arg0, arg1, letter1));
			}			
		} else {
			
			ShuffletterTile letter = this.board.get(arg0);
			this.board.remove(arg0);
			this.board.put(arg1,letter);
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.tileMoved(new ShuffletterTileMovedEvent(arg0, arg1, letter));
			}
			
		}
	}

	@Override
	public void play(ShuffletterTile arg0, Position arg1) {
		if(!supply.contains(arg0)){
			throw new IllegalArgumentException();		
		} 
		if(board.containsKey(arg1)) {
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.illegalMoveMade(new ShuffletterIllegalMoveEvent("Board contains tile in space"));
			}
		} else {
			this.board.put(arg1, arg0);
			this.supply.remove(arg0);
			for (ShuffletterModelListener listener : this.listeners)
			{
				listener.tilePlayed(new ShuffletterTilePlayedEvent(arg1, arg0));
			}
		}
	}

	@Override
	public void removeListener(ShuffletterModelListener arg0) {
		this.listeners.remove(arg0);		
	}
	
	
	/** Checks all words in the board that are connected vertically (up down) and horizontally (left to right)
	 * @return True if all words on board are correct, false otherwise
	 */
	private boolean checkwords() {
		
		Map<Integer,List<Position>> rows = new TreeMap<>();
		Map<Integer,List<Position>> columns = new TreeMap<>();

		/*Separates board into rows and columns in a Map, x position is key 
		for rows and y position is key for columns*/
		
		Set<Position> posset = this.board.keySet();
		for(Position pos : posset) {
			if(!rows.containsKey(pos.getY())){
				rows.put((Integer)pos.getY(), new ArrayList<Position>());
				rows.get(pos.getY()).add(pos);
				
			} else {
				rows.get(pos.getY()).add(pos);
			}
			if(!columns.containsKey(pos.getX())){	
				columns.put((Integer)pos.getX(), new ArrayList<Position>());
				columns.get(pos.getX()).add(pos);
			} else {
				columns.get(pos.getX()).add(pos);
			}
		}
		
		//Sorts lists of rows and columns from left to right (rows) and top to bottom (columns)
		
		Comparator<Position> compx = new Comparator<Position>() {
		    @Override
		    public int compare(Position o1, Position o2) {
		        return o1.getX() > o2.getX() ? 1:-1;
		    }
		}; 
		Comparator<Position> compy = new Comparator<Position>() {
		    @Override
		    public int compare(Position o1, Position o2) {
		        return o1.getY() < o2.getY() ? 1:-1;
		    }
		}; 
		Set<Integer> rowsk = rows.keySet();
		Set<Integer> columnsk = columns.keySet();

		for (Integer i : rowsk){
			Collections.sort(rows.get(i),compx);
		}
		for (Integer i : columnsk){
			Collections.sort(columns.get(i),compy);
		}
		
		//Checks if words in rows and columns are valid by passing them to dictcheck()
		
		List<StringBuilder> wildwords = new ArrayList<>();

		
		for(Integer i: rowsk) {
			if(!dictcheck(rows.get(i),true,wildwords)) {
				return false;
			}
		}
		
		for(Integer i :columnsk){
			if(!dictcheck(columns.get(i),false,wildwords)){
				return false;
			}
		}
		
		//check words with wildtiles by calling checkwildtiles()

		if(!checkwildtiles(wildwords)) {
			return false;
		}
		
		return true;
	
		
	}
	
	/** Takes a column or row of positions as a list
	 * and builds connected tiles from board into words
	 * Checks and returns whether or not words are valid
	 * @param ls The row or column of positions being checked as an organized list
	 * from left to right if row, top to bottom if column
	 * @param rowcol Specify true if checking a row of positions, false if column
	 * @param wildwords List of wildwords to add to given a space
	 * @return True if words are valid, false otherwise
	 */
	private boolean dictcheck(List<Position> ls, boolean rowcol,List<StringBuilder> wildwords) {
		
		
		List<StringBuilder> words = new ArrayList<>();
		
		Deque<Position> pos = new ArrayDeque<>();
		
		//Builds the words from the list of positions in a row or column
		
		for(Position p: ls) {
			
			pos.add(p);
		}

		while(!pos.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			Position proclet = pos.pop();
			sb.append(board.get(proclet).getLetter());
			
			if(rowcol){	
				while(!pos.isEmpty() && pos.peek().getX()== proclet.getX()+1){
					proclet = pos.pop();
					sb.append(board.get(proclet).getLetter());
				}
			
			} else {
				while(!pos.isEmpty() && pos.peek().getY()== proclet.getY()-1){
					proclet = pos.pop();
					sb.append(board.get(proclet).getLetter());
				}
			}
			words.add(sb);
		}
		
		
		//Checks if build words are valid
		
		for(StringBuilder s: words) {
			//System.out.println(s);
			if(s.length()<=1) {
				continue;
			}
			if(s.indexOf(" ")== -1){
				if(!wordlist.contains(s.toString().toLowerCase())) {
					return false;
				}
			} else{
				wildwords.add(s);
			}
		}		

		
		return true;
	}
	
	/** Checks a list of words that contain one wild tile 
	 * @param ls The list of words (StringBuilder objects) that contain one wild tile
	 * @return True if wild tile words are valid, false otherwise
	 */
	private boolean checkwildtiles(List<StringBuilder> ls) {
		List<List<Character>> wildpos = new LinkedList<>();
		//System.out.println(ls.size());
		if(ls.isEmpty()){
			return true;
		} else if(ls.size()==1) {
			List<String> newpos  = new LinkedList<>();
			int ind = ls.get(0).indexOf(" ");
			for(int i =0; i<26; i++){
				StringBuilder temp = new StringBuilder(ls.get(0).toString());
				char ch = (char) ('A'+i);
				temp.setCharAt(ind, ch);
				newpos.add(temp.toString());
			}
			for(int i=0; i<26; i++) {
				if(wordlist.contains(newpos.get(i).toString().toLowerCase())){
					return true;
				}
			}
			
		} else if(ls.size()>1) {
			int num =0;
			for(StringBuilder s: ls) {
				List<String> newpos  = new LinkedList<>();
				int ind = s.indexOf(" ");
				wildpos.add(new LinkedList<Character>());
				for(int i =0; i<26; i++){
					StringBuilder temp = new StringBuilder(s.toString());
					char ch = (char) ('A'+i);
					temp.setCharAt(ind, ch);
					newpos.add(temp.toString());
				}
				for(int i=0; i<26; i++) {
					if(wordlist.contains(newpos.get(i).toString().toLowerCase())){
						wildpos.get(num).add(newpos.get(i).charAt(ind));
					}
				}
				num++;
			}
			for(char c: wildpos.get(0)){
				if(wildpos.get(1).contains(c)){
					//System.out.println(c);
					return true;
				}
			}
		}
		return false;
	}
	
	/** Checks the board to see if all tiles are connected
	 * @return true if tiles connected, false otherwise
	 */
	private boolean checkorthogonal() {
		Set<Position> posset = this.board.keySet();
		
		Deque<Position> posque = new ArrayDeque<>();
		Deque<Position> allpos = new ArrayDeque<>();
		
		for(Position pos : posset) {
			allpos.push(pos);			
		}
		
		posque.push(allpos.pop());
		
		while(!posque.isEmpty()){
			Position currpos = posque.pop();
			Position pos1 = new Position(currpos.getX()+1,currpos.getY());
			Position pos2 = new Position(currpos.getX(),currpos.getY()+1);
			Position pos3 = new Position(currpos.getX(),currpos.getY()-1);
			Position pos4 = new Position(currpos.getX()-1,currpos.getY());
			if(allpos.contains(pos1)) {
				posque.add(pos1);
				allpos.remove(pos1);
			}
			if(allpos.contains(pos2)) {
				posque.add(pos2);
				allpos.remove(pos2);
			}
			if(allpos.contains(pos3)) {
				posque.add(pos3);
				allpos.remove(pos3);
			}
			if(allpos.contains(pos4)) {
				posque.add(pos4);
				allpos.remove(pos4);
			}
			
		}
		
		if(allpos.isEmpty()) {
			return true;
		}
		return false;
		
	}

}
