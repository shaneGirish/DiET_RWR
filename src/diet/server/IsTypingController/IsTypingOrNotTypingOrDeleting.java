/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.IsTypingController;

import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.Participant;
import diet.utils.HashtableWithDefaultvalue;
import diet.utils.VectorHashtableEnforcingUniqueness;
import java.awt.Color;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class IsTypingOrNotTypingOrDeleting extends Thread {

	VectorHashtableEnforcingUniqueness htPairsWhoAreInformedOfIsTyping = new VectorHashtableEnforcingUniqueness();

	final String constants_IsTyping = "ISTYPING";
	final String constants_IsDeleting = "ISDELETING";
	final String constants_NotTyping = "NOTTYPING";

	HashtableWithDefaultvalue htMode = new HashtableWithDefaultvalue(constants_NotTyping);
	HashtableWithDefaultvalue htLastTyping = new HashtableWithDefaultvalue(new Long(-1));

	long inactivitythreshold = 1000;
	public boolean doActivity = true;

	DefaultConversationController cC;

	public IsTypingOrNotTypingOrDeleting(DefaultConversationController cC) {
		this.cC = cC;
		this.start();
	}

	public void setInactivityThreshold(long threshold) {
		this.inactivitythreshold = threshold;
	}

	public void addPairWhoAreMutuallyInformedOfTyping(Participant a, Participant b) {
		this.htPairsWhoAreInformedOfIsTyping.put(a, b);
		this.htPairsWhoAreInformedOfIsTyping.put(b, a);
	}

	public void run() {
		while (doActivity) {
			try {
				Thread.sleep((long) inactivitythreshold / 3);
				// System.err.println("ITNT - WAKING UP");
			} catch (Exception e) {
				e.printStackTrace();
				Conversation.printWSlnLog("Main", constants_IsTyping);
			}
			examineInactivity();
		}
	}

	public synchronized void processTYPINGByClient(Participant p) {
		String mode = (String) htMode.getObject(p);
		this.htLastTyping.putObject(p, new Date().getTime());
		if (mode.equalsIgnoreCase(constants_IsTyping))
			return;
		htMode.putObject(p, constants_IsTyping);
		this.informISTYPING(p);

	}

	private synchronized void informISTYPING(Participant p) {
		// cC.getC().sendArtificialTurnToRecipient(p, "IS TYPING", 0);
		Vector v = this.htPairsWhoAreInformedOfIsTyping.get(p);
		for (int i = 0; i < v.size(); i++) {
			Participant pRecipient = (Participant) v.elementAt(i);
			cC.getConversation().changeClientInterface_changeBorderOnChatFrame(pRecipient, 6, Color.black);

		}
	}

	public synchronized void processDELETINGByClient(Participant p) {
		String mode = (String) htMode.getObject(p);
		this.htLastTyping.putObject(p, new Date().getTime());
		if (mode.equalsIgnoreCase(constants_IsDeleting))
			return;
		htMode.putObject(p, constants_IsDeleting);
		this.informISDELETING(p);

	}

	private synchronized void informISDELETING(Participant p) {
		// cC.getC().sendArtificialTurnToRecipient(p, "IS TYPING", 0);
		Vector v = this.htPairsWhoAreInformedOfIsTyping.get(p);
		for (int i = 0; i < v.size(); i++) {
			Participant pRecipient = (Participant) v.elementAt(i);
			cC.getConversation().changeClientInterface_changeBorderOnChatFrame(pRecipient, 6, Color.RED);

		}
	}

	private synchronized void examineInactivity() {
		Enumeration eee = htLastTyping.ht.keys();
		// System.err.println("EXAMINING ACTIVITY - 1");
		while (eee.hasMoreElements()) {
			Participant p = (Participant) eee.nextElement();
			Long timeOfLastTyping = (Long) htLastTyping.getObject(p);
			long inactivity = new Date().getTime() - timeOfLastTyping;
			String mode = (String) htMode.getObject(p);
			// System.err.println("EXAMINING ACTIVITY - 2 "+p.getUsername()+
			// "inactivity: "+inactivity+"
			// inactivitythreshold"+inactivitythreshold+" mode:"+mode);
			if (inactivity > inactivitythreshold & (mode.equalsIgnoreCase(this.constants_IsTyping)
					|| mode.equalsIgnoreCase(this.constants_IsDeleting))) {
				System.err.println("ITNT - EXAMINING ACTIVITY - 3 " + p.getUsername());
				this.informISNOTDOING(p);
				this.htMode.putObject(p, this.constants_NotTyping);
			}
		}

	}

	private synchronized void informISNOTDOING(Participant p) {
		Vector v = this.htPairsWhoAreInformedOfIsTyping.get(p);
		for (int i = 0; i < v.size(); i++) {
			Participant pRecipient = (Participant) v.elementAt(i);
			cC.getConversation().changeClientInterface_changeBorderOnChatFrame(pRecipient, 6, Color.white);

		}

	}

}
