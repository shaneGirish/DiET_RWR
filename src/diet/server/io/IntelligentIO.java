/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.io;

import diet.attribval.AttribVal;
import diet.client.ClientInterfaceEvents.ClientInterfaceEventStringPrettifier;
import diet.message.Message;
import diet.message.MessageClientInterfaceEvent;
import diet.message.MessageKeypressed;
import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.Participant;
import diet.server.conversationhistory.turn.ArtificialTurnProducedByServer;
import diet.server.conversationhistory.turn.DataToBeSaved;
import diet.server.conversationhistory.turn.Turn;
import diet.server.conversationhistory.turn.NormalTurnProducedByParticipant;
import diet.server.conversationhistory.turn.NormallTurnProducedByParticipantInterceptedByServer;
import diet.transcript.Transcripts;
import diet.utils.FilenameToolkit;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class IntelligentIO {

	File conversationDirectory;
	Hashtable htTextFiles = new Hashtable();
	Hashtable htObjectFiles = new Hashtable();
	Hashtable htThrowables = new Hashtable();
	Hashtable htSpreadsheetFiles = new Hashtable();
	String separator = DefaultConversationController.config.recordeddata_CSVSeparator;
	Conversation c;

	Transcripts ts;

	public IntelligentIO(Conversation c, String parentDir, String experimentDirectorySuffix) {
		this.c = c;
		ts = new Transcripts(c);
		File pDirF = new File(parentDir);
		String[] dirlist = pDirF.list();
		String prefixNumber = FilenameToolkit.getNextPrefixInteger(dirlist);
		conversationDirectory = new File(parentDir + File.separatorChar + prefixNumber + experimentDirectorySuffix);
		try {
			conversationDirectory.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void writeToTextFileCreatingIfNecessary(String filename, String text) {
		IntelligentTextFileWriter itfw = (IntelligentTextFileWriter) this.htTextFiles.get(filename);
		if (itfw == null) {
			itfw = new IntelligentTextFileWriter(new File(conversationDirectory, filename));
			this.htTextFiles.put(filename, itfw);
		}
		itfw.appendText(text);
	}

	public void writeToSpreadsheetFileWithAutoHeaderCreatingIfNecessary(String filename, Vector additionalValues) {
		IntelligentSpreadsheetAndHeaderWriter isfw = (IntelligentSpreadsheetAndHeaderWriter) this.htSpreadsheetFiles
				.get(filename);
		if (isfw == null) {
			isfw = new IntelligentSpreadsheetAndHeaderWriter(new File(conversationDirectory, filename));
			this.htSpreadsheetFiles.put(filename, isfw);
		}
		isfw.saveAttribVals(additionalValues);
	}

	public void writeToObjectFileCreatingIfNecessary(String filename, Object o) {
		if (DefaultConversationController.config.debug_debugTime)
			c.saveTime("5A");
		IntelligentObjectFileWriter iofw = (IntelligentObjectFileWriter) this.htObjectFiles.get(filename);
		if (DefaultConversationController.config.debug_debugTime)
			c.saveTime("5B");
		if (iofw == null) {
			iofw = new IntelligentObjectFileWriter(new File(conversationDirectory, filename));
			this.htObjectFiles.put(filename, iofw);
			if (DefaultConversationController.config.debug_debugTime)
				c.saveTime("5BBBBBBBBBB");
		}
		if (DefaultConversationController.config.debug_debugTime)
			c.saveTime("5C");
		iofw.saveObject(o);
		if (DefaultConversationController.config.debug_debugTime)
			c.saveTime("5D");
	}

	public void writeToThrowableFileCreatingIfNecessary(String filename, Throwable t) {
		IntelligentThrowableWriter itw = (IntelligentThrowableWriter) this.htThrowables.get(filename);
		if (itw == null) {
			itw = new IntelligentThrowableWriter(new File(conversationDirectory, filename));
			this.htTextFiles.put(filename, itw);
		}
		itw.saveThrowable(t);
	}

	public void saveWindowTextToLog(String windowName, String s) {
		this.writeToTextFileCreatingIfNecessary("window_" + windowName + ".txt", s + "\n");
		System.err.println("Saving window message:" + s);
	}

	public void saveTurn(Turn t) {

		try {
			this.writeToObjectFileCreatingIfNecessary("turnsserialized.obj", t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			saveTurnAsText(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			saveTurnAsAttribVals(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveTurnAsText(Turn t) {
		String line = "";
		if (t instanceof NormalTurnProducedByParticipant) {
			NormalTurnProducedByParticipant tpbp = (NormalTurnProducedByParticipant) t;
			line = line + tpbp.getCH().getConversationName() + separator;
			line = line + (new Date().getTime()) + separator;
			line = line + tpbp.getSubDialogueID() + separator;
			if (t instanceof NormallTurnProducedByParticipantInterceptedByServer) {
				line = line + "interceptedturn" + separator;
			} else {
				line = line + "normalturn" + separator;
			}
			line = line + tpbp.getSenderID() + separator;
			line = line + tpbp.getSender().getUsername() + separator;
			line = line + tpbp.getApparentSender().getUsername() + separator;
			line = line + tpbp.getText() + separator;
			line = line + tpbp.getRecipientsAsString() + separator;
			line = line + tpbp.getDocDeletes() + separator;
			line = line + tpbp.getKeypressDeletes() + separator;
			line = line + tpbp.getTimeOnClientOfStartTyping() + separator;
			line = line + tpbp.getTimeOnClientOfSending() + separator;
			line = line + tpbp.getTimeOnServerOfReceipt() + separator;
			line = line + ClientInterfaceEventStringPrettifier
					.getTextFormulationProcessRepresentation(tpbp.getClientInterfaceEvents()) + separator;
			line = line + ClientInterfaceEventStringPrettifier.getTextFormulationProcessRepresentationLOGARHYTHMICSPACE(
					tpbp.getClientInterfaceEvents()) + separator;
			line = line + tpbp.getIOAdditionalValues() + separator;

		} else if (t instanceof ArtificialTurnProducedByServer) {
			ArtificialTurnProducedByServer tpbs = (ArtificialTurnProducedByServer) t;
			line = line + tpbs.getCH().getConversationName() + separator;
			line = line + (new Date().getTime()) + separator;
			line = line + tpbs.getSubdialogueID() + separator;
			line = line + "artificialturn" + separator;
			line = line + "server" + separator;
			line = line + "server" + separator;
			line = line + tpbs.getApparentSender().getUsername() + separator;
			line = line + tpbs.getText() + separator;
			line = line + tpbs.getRecipientsAsString() + separator;
			line = line + " " + separator;
			line = line + " " + separator;
			line = line + " " + separator;
			line = line + " " + separator;
			line = line + tpbs.getTimeOnServerOfSending() + separator;
			line = line + " " + separator;
			line = line + " " + separator;
			line = line + tpbs.getIOAdditionalValues() + separator;

		} else if (t instanceof DataToBeSaved) {
			DataToBeSaved dtbs = (DataToBeSaved) t;
			line = line + dtbs.getCH().getConversationName() + separator;
			line = line + (new Date().getTime()) + separator;
			line = line + dtbs.getSubdialogueID() + separator;
			line = line + dtbs.dataType + separator;
			line = line + dtbs.getSenderID() + separator;
			line = line + dtbs.getSenderName() + separator;
			line = line + dtbs.getApparentSenderUsername() + separator;
			line = line + dtbs.getText() + separator;
			line = line + dtbs.getRecipientsAsString() + separator;
			line = line + " " + separator;
			line = line + " " + separator;
			line = line + dtbs.getTimeOnClientOfStartTyping() + separator;
			line = line + dtbs.getTimeOnClientOfSending() + separator;
			line = line + dtbs.getTimeOnServerOfRELAY() + separator;
			line = line + " " + separator;
			line = line + " " + separator;
			line = line + dtbs.getIOAdditionalValues() + separator;

		}

		this.writeToTextFileCreatingIfNecessary("turns.txt", line + "\n");
		// this.saveTurnAsAttribVals(t);

	}

	public void saveTurnAsAttribVals(Turn t) {
		String[] header = DefaultConversationController.config.recordeddata_mainoutputfileheader_HEADER;
		if (t instanceof NormalTurnProducedByParticipant) {
			Vector output = new Vector();
			NormalTurnProducedByParticipant tpbp = (NormalTurnProducedByParticipant) t;
			AttribVal av0 = new AttribVal(header[0], tpbp.getCH().getConversationName());
			AttribVal av1 = new AttribVal(header[1], new Date().getTime());
			AttribVal av2 = new AttribVal(header[2], tpbp.getSubDialogueID());
			AttribVal av3;
			if (t instanceof NormallTurnProducedByParticipantInterceptedByServer)
				av3 = new AttribVal(header[3], "interceptedturn");
			else
				av3 = new AttribVal(header[3], "normalturn");

			AttribVal av4 = new AttribVal(header[4], tpbp.getSenderID());
			AttribVal av5 = new AttribVal(header[5], tpbp.getSender().getUsername());
			AttribVal av6 = new AttribVal(header[6], tpbp.getApparentSender().getUsername());
			AttribVal av7 = new AttribVal(header[7], tpbp.getText());
			AttribVal av8 = new AttribVal(header[8], tpbp.getRecipientsAsString());
			AttribVal av9 = new AttribVal(header[9], tpbp.getDocDeletes());
			AttribVal av10 = new AttribVal(header[10], tpbp.getKeypressDeletes());
			AttribVal av11 = new AttribVal(header[11], tpbp.getTimeOnClientOfStartTyping());
			AttribVal av12 = new AttribVal(header[12], tpbp.getTimeOnClientOfSending());
			AttribVal av13 = new AttribVal(header[13], tpbp.getTimeOnServerOfReceipt());

			output.add(av0);
			output.add(av1);
			output.add(av2);
			output.add(av3);
			output.add(av4);
			output.add(av5);
			output.add(av6);
			output.add(av7);
			output.add(av8);
			output.add(av9);
			output.add(av10);
			output.add(av11);
			output.add(av12);
			output.add(av13);

			try {
				Vector newAttribVals = tpbp.getAdditionalValues();
				for (int i = 0; i < newAttribVals.size(); i++) {
					AttribVal avN = (AttribVal) newAttribVals.elementAt(i);
					output.add(avN);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.writeToSpreadsheetFileWithAutoHeaderCreatingIfNecessary("turnasattribvals.txt", output);

		} else if (t instanceof ArtificialTurnProducedByServer) {
			Vector output = new Vector();
			ArtificialTurnProducedByServer tpbs = (ArtificialTurnProducedByServer) t;
			AttribVal av0 = new AttribVal(header[0], tpbs.getCH().getConversationName());
			AttribVal av1 = new AttribVal(header[1], new Date().getTime());
			AttribVal av2 = new AttribVal(header[2], tpbs.getSubdialogueID());
			AttribVal av3 = new AttribVal(header[3], "normalturn");
			AttribVal av4 = new AttribVal(header[4], "server");
			AttribVal av5 = new AttribVal(header[5], "server");
			AttribVal av6 = new AttribVal(header[6], tpbs.getApparentSender().getUsername());
			AttribVal av7 = new AttribVal(header[7], tpbs.getText());
			AttribVal av8 = new AttribVal(header[8], tpbs.getRecipientsAsString());
			AttribVal av9 = new AttribVal(header[9], "");
			AttribVal av10 = new AttribVal(header[10], "");
			AttribVal av11 = new AttribVal(header[11], "");
			AttribVal av12 = new AttribVal(header[12], "");
			AttribVal av13 = new AttribVal(header[13], tpbs.getTimeOnServerOfSending());

			output.add(av0);
			output.add(av1);
			output.add(av2);
			output.add(av3);
			output.add(av4);
			output.add(av5);
			output.add(av6);
			output.add(av7);
			output.add(av8);
			output.add(av9);
			output.add(av10);
			output.add(av11);
			output.add(av12);
			output.add(av13);

			try {
				Vector newAttribVals = tpbs.getAdditionalValues();
				for (int i = 0; i < newAttribVals.size(); i++) {
					AttribVal avN = (AttribVal) newAttribVals.elementAt(i);
					output.add(avN);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.writeToSpreadsheetFileWithAutoHeaderCreatingIfNecessary("turnasattribvals.txt", output);

		} else if (t instanceof DataToBeSaved) {
			Vector output = new Vector();

			DataToBeSaved dtbs = (DataToBeSaved) t;
			AttribVal av0 = new AttribVal(header[0], dtbs.getCH().getConversationName());
			AttribVal av1 = new AttribVal(header[1], new Date().getTime());
			AttribVal av2 = new AttribVal(header[2], dtbs.getSubdialogueID());
			AttribVal av3 = new AttribVal(header[3], "data");

			AttribVal av4 = new AttribVal(header[4], dtbs.getSenderID());
			AttribVal av5 = new AttribVal(header[5], dtbs.getSenderName());
			AttribVal av6 = new AttribVal(header[6], dtbs.getApparentSenderUsername());
			AttribVal av7 = new AttribVal(header[7], dtbs.getText());
			AttribVal av8 = new AttribVal(header[8], dtbs.getRecipientsAsString());

			AttribVal av9 = new AttribVal(header[9], "");
			AttribVal av10 = new AttribVal(header[10], "");

			AttribVal av11 = new AttribVal(header[11], dtbs.getTimeOnClientOfStartTyping());
			AttribVal av12 = new AttribVal(header[12], dtbs.getTimeOnClientOfSending());

			AttribVal av13 = new AttribVal(header[13], dtbs.getTimeOnServerOfRELAY());

			output.add(av0);
			output.add(av1);
			output.add(av2);
			output.add(av3);
			output.add(av4);
			output.add(av5);
			output.add(av6);
			output.add(av7);
			output.add(av8);
			output.add(av9);
			output.add(av10);
			output.add(av11);
			output.add(av12);
			output.add(av13);

			try {
				Vector newAttribVals = dtbs.getAdditionalValues();
				for (int i = 0; i < newAttribVals.size(); i++) {
					AttribVal avN = (AttribVal) newAttribVals.elementAt(i);
					output.add(avN);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.writeToSpreadsheetFileWithAutoHeaderCreatingIfNecessary("turnasattribvals.txt", output);

		}

	}

	public void processTranscript(MessageClientInterfaceEvent mcie) {
		try {
			Participant sender = c.getParticipants().findParticipantWithEmail(mcie.getEmail());
			ts.processClientInterfaceEvent(sender, mcie);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	long debugcounter = 0;

	public void saveClientEvent(MessageClientInterfaceEvent mcie) {
		processTranscript(mcie);

		try {
			this.writeToObjectFileCreatingIfNecessary("clientinterfaceeventsserialized.obj", mcie);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			String eventtype = mcie.getClientInterfaceEvent().getType();
			AttribVal[] attribvals = mcie.getClientInterfaceEvent().getAttribVals();

			Vector v = new Vector();
			v.addElement(new AttribVal("senderID", mcie.getClientInterfaceEvent().participantID));
			v.addElement(new AttribVal("username", mcie.getClientInterfaceEvent().participantUsername));
			v.addElement(new AttribVal("eventtype", eventtype));
			v.addElement(new AttribVal("debugcounter", debugcounter));
			debugcounter++;

			v.addAll(new Vector(Arrays.asList(attribvals)));

			this.writeToSpreadsheetFileWithAutoHeaderCreatingIfNecessary("clientinterfaceevents.txt", v);

			String debugLine = mcie.getEmail() + "," + mcie.getUsername() + ","
					+ mcie.getClientInterfaceEvent().getType();
			this.writeToTextFileCreatingIfNecessary("debuggingmessageclientevent.txt", debugLine + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutThreadDownAndCloseFiles() {

	}

	public void saveMessage(Message m) {
		this.writeToObjectFileCreatingIfNecessary("messages.obj", m);
	}

	public void saveErrorLog(Throwable t) {
		this.writeToThrowableFileCreatingIfNecessary("errors-javastacktraces.txt", t);

	}

	public void saveErrorLog(String s) {
		this.writeToTextFileCreatingIfNecessary("errors.txt", s);
	}

	public void saveTextToFileCreatingIfNecessary(String s, String filename) {
		this.writeToTextFileCreatingIfNecessary(filename, s);
	}

	public File getFileNameContainingConversationData() {
		return conversationDirectory;
	}

	public void saveClientKeypressFromClient(MessageKeypressed mkp, String subDialogueID, String sender) {
		// this.saveDataToFile(subDialogueID,
		// sender.getParticipantID(),sender.getUsername(),
		// mkp.getTimeOfSending().getTime(), mkp.getTimeOfSending(),
		// mkp.getTimeOfReceipt().getTime()mkp.getContentsOfTextEntryWindow(),null);
		String line = "";
		line = line + c.getController().config.param_experimentID + separator;
		line = line + subDialogueID + separator;
		line = line + mkp.getTimeOfSending().getTime() + separator;
		line = line + mkp.getTimeOfReceipt().getTime() + separator;
		line = line + mkp.getContentsOfTextEntryWindow() + separator + "\n";
		this.writeToTextFileCreatingIfNecessary("textentrykeypresses.txt", line);
	}

	public void saveClientDocumentChange(String experimentID, String subdialogueID, Participant sender,
			long timeOfCreationOnClient, long timeOfReceiptOnServer, String contentsOfWindow) {
		// this.saveAdditionalRowOfDataToSpreadsheetOfTurns("KEYPRESSDOCCHANGE",
		// sender.getParticipantID(),sender.getUsername(),timeOfCreationOnClient
		// ,timestamp, timestamp, contentsOfWindow,null);
		String line = "";
		line = line + experimentID + separator;
		line = line + subdialogueID + separator;
		line = line + sender.getParticipantID() + separator;
		line = line + sender.getUsername() + separator;
		line = line + timeOfCreationOnClient + separator;
		line = line + timeOfReceiptOnServer + separator;
		line = line + contentsOfWindow + separator;
		line = line + "\n";

		this.writeToTextFileCreatingIfNecessary("textentrydocchanges.txt", line);

	}

	public void saveDocChange(String s) {
		this.writeToTextFileCreatingIfNecessary("docchanges.txt", s);
	}

}
