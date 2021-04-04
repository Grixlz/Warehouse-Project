//==================================================================
// The main purpose of this section is to keep a unique ID for each
// each every object. For unique IDs, do the followinging:
// [String] = [String] + (IDServer.instance()).generateID()
//==================================================================
import java.io.*;

public class IDServer implements Serializable {
	private static final long serialVersionUID = 1L;
 	private int sIdCounter;
  	private int cIdCounter;
  	private int pIdCounter;
	private static IDServer server;
	private IDServer() {
    sIdCounter = 1;
    cIdCounter = 1;
    pIdCounter = 1;
	}

  // References the ID server, or creates it if not found.
	public static IDServer instance() {
		if (server == null) {
			return (server = new IDServer());
		} else {
			return server;
		}
	}//end IDServer instance()

  // Creates a unique ID and increments the counter.
  public int generateCID() {
		return cIdCounter++;
	}

  public int generatePID() {
		return pIdCounter++;
	}

  public int generateSID() {
		return sIdCounter++;
	}

  // Displays the current ID counter.
	public String toString() {
		return ("IdServer" +
    "\nClient ID Counter: " + cIdCounter +
    "\nProduct ID Counter: " + pIdCounter +
    "\nSupplier ID Counter: " + sIdCounter);
	}
//==================================================================
// Misc
//==================================================================
	public static void retrieve(ObjectInputStream input) throws IOException {
		try {
			server = (IDServer) input.readObject();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception cnfe) {
			cnfe.printStackTrace();
		}
	}//end retrieve

	private void writeObject(java.io.ObjectOutputStream output) throws IOException {
    try {
      output.defaultWriteObject();
      output.writeObject(server);
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }//end writeObject

	private void readObject(java.io.ObjectInputStream input) throws IOException, ClassNotFoundException {
		try {
			input.defaultReadObject();
			if (server == null) {
				server = (IDServer) input.readObject();
			} else {
				input.readObject();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}//end readObject
}