package application;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;


public class Powerball {

	LinkedHashMap<Date,int[]> winningNumbers;



	public void importWinningNumbers() {

		winningNumbers = new LinkedHashMap<Date,int[]>();

		Charset charset = Charset.forName("UTF-8");
		Path filepath = Paths.get("bin/application", "winners.txt");

		try {
			BufferedReader reader = Files.newBufferedReader(filepath, charset);
			String currLine = null;
			reader.readLine(); //Skips over 1st line

			while((currLine = reader.readLine()) != null){ //While text...

				String[] tempStr = currLine.split(" "); //splits text by whitespace
				ArrayList<String> tempArrList = new ArrayList<String>();

				for(int i = 0; i < tempStr.length; i++){ //Done to eliminate empty strs caused by split by whitespace
					if(tempStr[i].compareTo("") != 0){
						tempArrList.add(tempStr[i]);
					}
				}

				SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");	//All of this to convert 1st tempArrList to Date - ISSUE RESOLVED, LinkedHashMap deleted Date b/c wrong formatting
				Date dateIn = null;

				try {
					dateIn = f.parse(tempArrList.get(0));
				} catch (ParseException e){
					e.printStackTrace();
				}                                                           //ends here

				int[] finalIn = new int[6];                                //Finally, get last 6 numbers
				//System.out.println(Arrays.toString(tempArrList.toArray()) + "    " + tempArrList.size() + "    ");
				for(int i = 1; i < 7; i++){               //hardcoding iteration through tempArrayList to prevent PP number being used
					finalIn[i-1] = Integer.valueOf(tempArrList.get(i));
				}
				//System.out.println(Arrays.toString(finalIn) + "    " + finalIn.length);

				winningNumbers.put(dateIn, finalIn);
				//System.out.println(winningNumbers.size());
				//System.out.println(Arrays.toString((int [])(winningNumbers.values().toArray()[winningNumbers.size()-1])));

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int[] genPowerballNums(){ //gens PowerBallNums, not necessarily checked though

		Random r = new Random();
		int[] powerballNums = new int[6];
		/*
		for(int i = 0; i < 5; i++) {
			powerballNums[i] = r.nextInt(68) + 1;
		}
		powerballNums[5] = r.nextInt(25) + 1;*/

		HashSet<Integer> s = new HashSet<Integer>(); //using Set, since duplicates cannot exist

		while(s.size() < 5){

			int randInt = r.nextInt(68) + 1;
			boolean testInt = s.add(randInt);
			if(testInt != false){
				powerballNums[s.size()-1] = randInt;
			}
		}
		powerballNums[5] = (r.nextInt(25)+1);


		return powerballNums;
	}

	public boolean checkOldPBNums(int[] powerballNums){

		boolean isNew = true;
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy"); //used for conversion

		int[] tempPBNums = Arrays.copyOf(powerballNums, 5); //Because for the 1st 5, order doesn't matter, but Powerball does (6th #)
		int[] tempWinNums;

		for(int i = 0; i < winningNumbers.values().toArray().length; i++){
			int[] winningNumber = (int [])(winningNumbers.values().toArray()[i]);
			tempWinNums = Arrays.copyOf(winningNumber,5);
			Arrays.sort(tempWinNums); //sorts numbers for comparison, since order doesn't matter
			Arrays.sort(tempPBNums);
			if(Arrays.equals(tempWinNums, tempPBNums) == true && winningNumber[5] == powerballNums[5]){ //so, if the ordered first 5 are equal AND the powerball number is the same, its a repeat
				isNew = false;
				//System.out.println(Arrays.toString((int [])(winningNumbers.values().toArray()[i])));
			}
		}
//		if(isNew == true){
//
//			System.out.println(Arrays.toString(powerballNums));
//
//		}

		return isNew;
	}

	public int[][] multiPBNums(int num){ //takes number of numbers you need

		int[][] multiPBNums = new int[num][];

		for(int i = 0; i < num; i++){
			int[] powerballNums = genPowerballNums();
			while(checkOldPBNums(powerballNums) == false){
				powerballNums = genPowerballNums();
			}
			multiPBNums[i] = powerballNums;
		}
		return multiPBNums;
	}

	public void update() {

        URL url;
		try {
			url = new URL("http://www.powerball.com/powerball/winnums-text.txt");
	        ReadableByteChannel rbc1 = Channels.newChannel(url.openStream());
	        ReadableByteChannel rbc2 = Channels.newChannel(url.openStream());
	        FileOutputStream fos1 = new FileOutputStream("src/application/winners.txt");
	        FileOutputStream fos2 = new FileOutputStream("bin/application/winners.txt");
	        fos1.getChannel().transferFrom(rbc1, 0, Long.MAX_VALUE);
	        fos2.getChannel().transferFrom(rbc2, 0, Long.MAX_VALUE);
	        fos1.close();
	        fos2.close();
	        rbc1.close();
	        rbc2.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Update failed... ask Luis lol");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Update failed... ask Luis lol");
			e.printStackTrace();
		}

		System.out.println("Update successful!");

	}

	public String latestDate() { //added latestDate, update, look at those for error. @initializer.

		String ld = new String();
		SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
		ld = "Latest Powerball Date: " + f.format((winningNumbers.keySet().toArray()[0]));
		//System.out.println(ld);
		return ld;
	}

}
