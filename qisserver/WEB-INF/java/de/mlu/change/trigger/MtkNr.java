package de.mlu.change.trigger;

import org.apache.log4j.Logger;

public class MtkNr {
	private int mtknr = -1;
	private int prfZif = -1;
	static final Logger logger = Logger.getLogger(MtkNr.class);
	
	public void calculateNewMtknr(String lastMtknr, String prfZiffVerfahren) {
		int mtknrInt = Integer.parseInt(lastMtknr.substring(0, 8));
		switch(prfZiffVerfahren) {
			case "J": {
				this.quersumme(lastMtknr);
				break;
			}
		}
		this.mtknr = mtknrInt + 1;
		logger.debug(lastMtknr + " => " + this.mtknr + " + "+ this.prfZif + " => " + this.toString());
	}

	private void quersumme(String lastMtknr) {
		int[] faktoren = {1,7,3,1,7,3,1,7,3};
		int quersumme = 0;
		for(int i = 0; i < 8; i++) {
			int ziffer = Integer.parseInt(lastMtknr.substring(i, i+1));
			int faktor = faktoren[i];
			quersumme = quersumme + (faktor * ziffer);
		}
		this.prfZif = quersumme % 10;
		if(this.prfZif > 0) {
			this.prfZif = (10 - this.prfZif);
		}
	}
	
	public int getMtknrWithPrfziff() {
		return Integer.parseInt(Integer.toString(this.mtknr) + Integer.toString(this.prfZif));
	}
	
	public int getMtknr() {
		return this.mtknr;
	}
	
	public int getPrfziff() {
		return this.prfZif;
	}
	
	public String toString() {
		return Integer.toString(this.mtknr) + Integer.toString(this.prfZif);
	}
	
	public Integer toInteger() {
		return Integer.valueOf(this.mtknr + "" + this.prfZif);
	}
}
