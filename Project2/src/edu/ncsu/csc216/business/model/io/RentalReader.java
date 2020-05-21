/**
 * 
 */
package edu.ncsu.csc216.business.model.io;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.Scanner;

import edu.ncsu.csc216.business.list_utils.SimpleArrayList;
import edu.ncsu.csc216.business.model.properties.RentalUnit;
import edu.ncsu.csc216.business.model.stakeholders.Client;
import edu.ncsu.csc216.business.model.stakeholders.PropertyManager;

/**
 * This class reads Rental Data files and adds data from file to PropertyManager
 * fields
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 * 
 */
public class RentalReader {

	private static PropertyManager p = PropertyManager.getInstance();

	/**
	 * Reads the rental data from the file and adds the data using PropertyManager
	 * objects
	 * 
	 * @param fileName the filename that the rental data is being read from
	 * @throws IllegalArgumentException with the message “Unable to load file.” if
	 *                                  the file cannot be loaded.
	 */
	public static void readRentalData(String fileName) {
		Scanner fileReader;
		String[] splitArray;
		String fileString = "\n";
		p.flushAllData();

		try {
			fileReader = new Scanner(new FileInputStream(fileName));

			while (fileReader.hasNextLine()) {
				fileString += fileReader.nextLine();
				fileString += "\n";
			}
			fileReader.close();
			boolean isOnlyClients = true;
			String[] clientArr = fileString.split("[\\r\\n]+");
			for (int i = 0; i < clientArr.length; i++) {
				if (!clientArr[i].isEmpty() && !clientArr[i].startsWith("#")) {
					isOnlyClients = false;
				}
			}

			splitArray = fileString.split("\\r?\\n[#]");

			SimpleArrayList<String> everythingArray = new SimpleArrayList<String>();
			for (int i = 0; i < splitArray.length; i++) {
				if (!splitArray[i].isEmpty() || !splitArray[i].isBlank()) {
					everythingArray.add(splitArray[i]);
				}
			}

			if (isOnlyClients) {
				for (int i = 0; i < everythingArray.size(); i++) {
					processClient(everythingArray.get(i).trim());
				}
				return;
			}

			String[] ruArray = everythingArray.get(0).strip().split("[\\r\\n]+");
			for (int i = 0; i < ruArray.length; i++) {
				processRentalUnit(ruArray[i]);
			}

			for (int i = 1; i < everythingArray.size(); i++) {
				processClient(everythingArray.get(i).trim());
			}

		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to load file.");
		}
	}

	/**
	 * 
	 * Processes a rental unit string and adds it to the instance of property
	 * manager if it is a valid unit, else an exception is thrown. Does nothing for
	 * empty or blank string.
	 * 
	 * @param s string that is being turned into a rental unit
	 * @throws IllegalArgumentException with the message "Couldn't process Rental
	 *                                  Unit" if it is an invalid rental unit
	 */
	private static void processRentalUnit(String s) {
		String hs = "Hotel Suite";
		String o = "Office";
		String cr = "Conference Room";

		try {
			String[] ruArr = s.split("\\s+");

			if (ruArr.length > 6) {
				throw new IllegalArgumentException();
			}

			int i = 0;
			for (i = 0; i < ruArr.length; i++) {
				if (!ruArr[i].isEmpty() || !ruArr[i].isBlank()) {
					break;
				}
			}

			if (ruArr[i].startsWith("H")) {
				p.addNewUnit(hs, ruArr[i + 2].trim(), Integer.parseInt(ruArr[i + 4].trim()));
				if (s.contains("Unavailable")) {
					p.getUnitAtLocation(ruArr[i + 2].trim()).takeOutOfService();
				}
			}
			if (ruArr[i].startsWith("O")) {
				p.addNewUnit(o, ruArr[i + 1].trim(), Integer.parseInt(ruArr[i + 3].trim()));
				if (s.contains("Unavailable")) {
					p.getUnitAtLocation(ruArr[i + 1].trim()).takeOutOfService();
				}
			}
			if (ruArr[i].startsWith("C")) {
				p.addNewUnit(cr, ruArr[i + 2].trim(), Integer.parseInt(ruArr[i + 4].trim()));
				if (s.contains("Unavailable")) {
					p.getUnitAtLocation(ruArr[i + 2].trim()).takeOutOfService();
				}

			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Couldn't process Rental Unit");
		}
	}

	/**
	 * Processes a client string with leases if applicable and adds it to the
	 * instance of property manager if it is a valid client, else an exception is
	 * thrown. Does nothing for empty or blank string.
	 * 
	 * @param s string that is being turned into a client with leases if applicable
	 * @throws IllegalArgumentException with the message "Couldn't process Client"
	 *                                  if it is an invalid rental unit
	 */
	private static void processClient(String s) {
		try {
			String[] client = s.split("[\\r\\n]+");

			SimpleArrayList<String> clientArr = new SimpleArrayList<String>();

			int i = 0;
			for (i = 0; i < client.length; i++) {
				if (!client[i].isEmpty()) {
					clientArr.add(client[i].trim());
				}
			}

			if (clientArr.size() == 0) {
				return;
			}

			int idxOfParan = clientArr.get(0).indexOf("(");
			if (idxOfParan < 0) {
				throw new IllegalArgumentException("couldnt process client");
			}

			p.addNewClient(clientArr.get(0).trim().substring(0, idxOfParan),
					clientArr.get(0).trim().substring(idxOfParan + 1, clientArr.get(0).length() - 1));

			Client c = new Client(clientArr.get(0).trim().substring(0, idxOfParan),
					clientArr.get(0).trim().substring(idxOfParan + 1, clientArr.get(0).length() - 1));

			if (clientArr.size() > 1) {
				for (i = 1; i < clientArr.size(); i++) {

					String[] leaseArr = clientArr.get(i).split("\\|");
					SimpleArrayList<String> leaseCleanArr = new SimpleArrayList<String>();
					for (int j = 0; j < leaseArr.length; j++) {
						if (!leaseArr[j].isEmpty() || !leaseArr[j].isBlank()) {
							leaseCleanArr.add(leaseArr[j]);
						}
					}
					int confNum = Integer.parseInt(leaseCleanArr.get(0).trim());
					LocalDate start = LocalDate.parse(leaseCleanArr.get(1).trim().substring(0, 10));
					LocalDate end = LocalDate.parse(leaseCleanArr.get(1).trim().substring(14, 24));
					int numOcc = Integer.parseInt(leaseCleanArr.get(2).trim());
					String loc = leaseCleanArr.get(3).trim().substring(leaseCleanArr.get(3).length() - 6).trim();
					RentalUnit r = p.getUnitAtLocation(loc);
					if (r.isInService()) {
						p.addLeaseFromFile(c, confNum, r, start, end, numOcc);
					}
					if (!r.isInService()) {
						r.returnToService();
						p.addLeaseFromFile(c, confNum, r, start, end, numOcc);
						r.takeOutOfService();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Couldn't process Client");
		}
	}

}
