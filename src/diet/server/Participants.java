package diet.server;

import java.util.Vector;

import diet.message.Message;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageClientCloseDown;
import diet.message.MessageDisplayChangeJProgressBar;
import diet.message.MessageDisplayChangeWebpage;
import diet.message.MessageDisplayCloseWindow;
import diet.message.MessageDisplayNEWWebpage;

import diet.server.ConversationController.DefaultConversationController;
import java.awt.Color;

/**
 * Contains all the participants of a Conversation. It constructs messages to be
 * sent to the Participants and also polls the individual particpants for
 * incoming messages, placing them in a queue.
 * <p>
 * Please avoid invoking these methods directly...use the methods supplied in
 * {@link Conversation} class as they ensure that the messages are saved
 * correctly to the user interface.
 * 
 * @author user
 */

public class Participants {

	private Vector participants = new Vector();
	private Conversation c;
	// private Vector incomingMessages = new Vector();
	private QueueBlockingForRecipientSortsIncomingMessages incomingMessageQueue = new QueueBlockingForRecipientSortsIncomingMessages();

	private boolean enqueingMessages = true;

	public Participants(Conversation c) {
		this.c = c;

	}

	/**
	 * Returns next message from the queue of incoming messages. Blocks if no
	 * message available.
	 * 
	 * @return next mesage from participants
	 */
	public Message getNextMessage() {
		if (DefaultConversationController.config.debug_debugTime)
			Conversation.statC.saveTime("A");
		Message m = getNextMessageBlocking();
		if (DefaultConversationController.config.debug_debugTime)
			Conversation.statC.saveTime("B");
		if (m instanceof MessageChatTextFromClient && diet.debug.Debug.debugtimers) {
			((MessageChatTextFromClient) m).saveTime("serverParticipants.loadedbynewthreadfromqueue");
		}

		return m;
	}

	/**
	 * 
	 * Returns next message from the queue of incoming messages. Blocks if no
	 * message available.
	 * 
	 * @return next message from participants
	 */
	private Message getNextMessageBlocking() {
		return this.incomingMessageQueue.getNextMessageBlocking();
	}

	/**
	 * Adds message to queue of incoming messages
	 * 
	 * @param m
	 *            Message from participant
	 */
	public void addMessageToIncomingQueue(Message m) {
		if (m instanceof MessageChatTextFromClient && diet.debug.Debug.debugtimers) {
			((MessageChatTextFromClient) m).saveTime("PARTICIPANTS-PREADDINGTOSYNCHRONIZED");
		}
		this.incomingMessageQueue.addMessageToIncomingQueue(m);
	}

	/**
	 * Loop which polls all participants for incoming messages
	 * 
	 */

	/**
	 * 
	 * @return Vector containing all participants in the experiment (including
	 *         inactive participants)
	 */
	public Vector getAllParticipants() {
		return participants;
	}

	public Vector getAllParticipantsNewCopy() {
		Vector v = new Vector();
		for (int i = 0; i < participants.size(); i++) {
			v.addElement(participants.elementAt(i));
		}
		return v;
	}

	/**
	 * 
	 * @return Vector only containing participants that have active connections.
	 */
	public Vector<Participant> getAllActiveParticipants() {
		Vector<Participant> v = new Vector<>();
		for (int i = 0; i < participants.size(); i++) {
			Participant p = (Participant) participants.elementAt(i);
			if (p.isConnected()) {
				v.addElement(p);
			}
		}
		return v;
	}

	/**
	 * Adds participant to the Vector containing all the participants that are
	 * polled for incoming messages and that are sent messages.
	 * 
	 * 
	 * @param Participant
	 *            that has just joined the Conversation and completed login
	 */
	public void addNewParticipant(Participant p) {
		participants.addElement(p);

		// c.getPermissionsController().addConnectingToAllInTheirNextFreeWindow(p,ownWindowNumber,permissionsEnabled);
	}

	/**
	 * Searches for a participant by email (case insensitive)
	 * 
	 * @param email
	 *            to search for
	 * @return null | Participant
	 */
	public Participant findParticipantWithEmail(String participantemail) {
		for (int i = 0; i < participants.size(); i++) {

			Participant p = (Participant) participants.elementAt(i);

			if (p.getParticipantID().equalsIgnoreCase(participantemail))
				return p;
		}

		return null;
	}

	/**
	 * Searches for a participant by username (case insensitive)
	 * 
	 * @param email
	 *            to search for
	 * @return null | Participant
	 */
	public Participant findParticipantWithUsername(String participantusername) {
		for (int i = 0; i < participants.size(); i++) {
			Participant p = (Participant) participants.elementAt(i);
			if (p.getUsername().equalsIgnoreCase(participantusername))
				return p;
		}
		return null;
	}

	/**
	 * Sends a turn to the specified recipients. The turn can either be sent by
	 * another participant or by the "server", in which case it is an
	 * intervention.
	 * 
	 * 
	 * @param recipients
	 *            Vector containing all the Participants who are to receive the
	 *            intervention
	 * @param senderEmail
	 *            String describing the email of the sender. If it is an
	 *            intervention, the email will be server
	 * @param senderUsername
	 *            String describing the username of the sender. If it is an
	 *            intervention, the username will be "sender".
	 * @param type
	 *            NOT USED
	 * @param text
	 *            text to be displayed in the chat window
	 * @param windowNumbers
	 *            Vector containing info for each client in which chat text is
	 *            to appear. In default mode this should be 0.
	 * @param prefixUsername
	 *            String which is prefixed at the beginning of the turn
	 *            (apparent origin)
	 */
	public void sendChatTextToParticipantsDEPRECATED(Vector recipients, String senderEmail, String senderUsername,
			int type, String text, Vector windowNumbers, boolean prefixUsername) {
		for (int i = 0; i < recipients.size(); i++) {
			Participant p = (Participant) recipients.elementAt(i);
			int windowNumber = (Integer) windowNumbers.elementAt(i);
			// MessageChatTextToClient msccttc = new
			// MessageChatTextToClient(senderEmail,senderUsername,0,text,windowNumber,prefixUsername);
			// msccttc.timeStamp();
			// p.sendMessage(msccttc);
		}
	}

	/**
	 * Sends message to individual participant
	 * 
	 * @param p
	 *            recipient
	 * @param m
	 *            sender
	 */
	public void sendMessageToParticipant(Participant p, Message m) {
		// System.out.println("SINL"+p.getUsername());

		// m.setTimeOnServerOfReceipt();
		// System.out.println("SINM"+p.getUsername());
		p.sendMessage(m);
		// System.out.println("SINN"+p.getUsername());
	}

	/**
	 * Returns a Vector containing all other participants taking part in the
	 * Conversation
	 * 
	 * @param p
	 *            participant to search for
	 * @return Vector containing all the other participants
	 */
	public Vector getAllOtherParticipants(Participant p) {
		Vector v = new Vector();
		v.addAll(participants);
		v.remove(p);
		return v;
	}

	public void killClient(Participant p) {
		try {
			p.sendMessage(new MessageClientCloseDown("server", "server"));
			p.getConnection().setConnected(false);
		} catch (Exception e) {
			e.printStackTrace();
			Conversation.printWSln("Main", "Server killed client " + p.getUsername());
		}

	}

	/**
	 * Attempts to close down the Thread which loops through all the
	 * participants, polling them for incoming messages and also attempts to
	 * close down the individual participants' incoming and outgoing
	 * connections.
	 * 
	 * @param sendCloseToClients
	 */
	public void closeDown(boolean sendCloseToClients) {
		for (int i = 0; i < participants.size(); i++) {
			Participant p = (Participant) participants.elementAt(i);
			try {
				if (sendCloseToClients)
					p.sendMessage(new MessageClientCloseDown("server", "server"));
				p.getConnection().setConnected(false);
			} catch (Exception e) {
			}
		}
		enqueingMessages = false;
		participants = null;
		c = null;
		incomingMessageQueue = null;
	}

	public void displayNEWWebpage(Participant p, String id, String header, String url, int width, int height,
			boolean vScrollBar, boolean forceCourierFont) {
		MessageDisplayNEWWebpage mdnw = new MessageDisplayNEWWebpage(id, header, url, width, height, vScrollBar, false);
		this.sendMessageToParticipant(p, mdnw);
	}

	public void changeWebpage(Participant p, String id, String url) {
		MessageDisplayChangeWebpage mdcw = new MessageDisplayChangeWebpage(id, url);
		this.sendMessageToParticipant(p, mdcw);
	}

	public void changeWebpage(Participant p, String id, String url, String prepend, String append) {
		MessageDisplayChangeWebpage mdcw = new MessageDisplayChangeWebpage(id, url, prepend, append);
		this.sendMessageToParticipant(p, mdcw);
	}

	public void changeWebpage(Participant p, String id, String url, String prepend, String append, long lengthOfTime) {
		MessageDisplayChangeWebpage mdcw = new MessageDisplayChangeWebpage(id, url, prepend, append, lengthOfTime);
		this.sendMessageToParticipant(p, mdcw);
	}

	public void changeJProgressBar(Participant p, String id, String text, Color foreCol, int value) {
		// System.out.println("SINI");
		MessageDisplayChangeJProgressBar mdcjpb = new MessageDisplayChangeJProgressBar(id, text, foreCol, value);
		// System.out.println("SINJ");
		this.sendMessageToParticipant(p, mdcjpb);
		// System.out.println("SINK");
	}

	public void closeWebpageWindow(Participant p, String id) {
		MessageDisplayCloseWindow mdcw = new MessageDisplayCloseWindow(id);
		this.sendMessageToParticipant(p, mdcw);
	}

	public synchronized void changeParticipants(Vector newList) {
		if (newList.size() != this.participants.size()) {
			System.err.println("NOT GOOD - SORTEDPARTICIPANTS DOESNT HAVE SAME SIZE " + newList.size() + " vs. "
					+ participants.size());
			Conversation.printWSln("Main", "NOT GOOD - SORTEDPARTICIPANTS DOESNT HAVE SAME SIZE " + newList.size()
					+ " vs. " + participants.size());

		}
		this.participants = newList;
	}

}
