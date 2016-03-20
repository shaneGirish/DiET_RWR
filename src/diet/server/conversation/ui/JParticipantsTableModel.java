/*
 * JTurnsListTableModel.java
 *
 * Created on 06 October 2007, 16:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversation.ui;

import diet.server.Conversation;
import diet.server.Participant;
import diet.server.ParticipantConnection;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import diet.server.conversationhistory.ConversationHistory;
import java.util.Date;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class JParticipantsTableModel extends AbstractTableModel {

	private ConversationHistory cH;
	private Conversation c;

	TableModel thisTableModel;
	JTable jt;
	UIUpdatingThread uit;

	public JParticipantsTableModel(JTable jt, ConversationHistory cH) {
		super();
		this.cH = cH;
		this.c = cH.getConversation();

		this.jt = jt;
		this.thisTableModel = this;
		uit = new UIUpdatingThread();
		uit.start();
	}

	// data.addElement(v);
	// refresh();

	public boolean isCellEditable(int x, int y) {
		return false;
	}

	public String getColumnName(int column) {
		if (column == 0) {
			return "Participant ID";
		} else if (column == 1) {
			return "Username";
		} else if (column == 2) {
			return "Subdialogue ID";
		} else if (column == 3) {
			return "IP Address";
		} else if (column == 4) {
			return "No. of (re)connections";
		} else if (column == 5) {
			return "No. of chat messages sent";
		} else if (column == 6) {
			return "Time since last activity";
		} else if (column == 7) {
			return "Time since last turn sent";
		}

		else {
			return " ";
		}
	}

	public Object getValueAt(int x, int y) {

		try {
			Participant p = (Participant) c.getParticipants().getAllParticipants().elementAt(x);
			ParticipantConnection pc = p.getConnection();

			if (y == 0) {
				return p.getParticipantID();
			}
			if (y == 1) {
				return p.getUsername();
			}
			if (y == 2) {
				return c.getController().participantPartnering.getSubdialogueID(p);
			} else if (y == 3) {
				return pc.getClientIPAddress();
			} else if (y == 4) {
				return p.getNumberOfConnections();
			} else if (y == 5) {
				return pc.getNumberOfChatMessagesProduced();
			} else if (y == 6) {
				if (pc.isConnected())
					return (((new Date().getTime() - pc.getTimeOfLastMessageSentToServer())) / 1000) + " secs";
				return "Not fully connected yet";
			} else if (y == 7) {
				if (pc.numberOfChatTurnsReceivedFromClient > 0)
					return (((new Date().getTime() - pc.getTimeOfLastChatTextSentToServer())) / 1000) + " secs";
				return "Hasn't sent any chat text yet";
			} else {
				return "";

			}

		} catch (Exception e) {
			e.printStackTrace();
			return "UI ERROR";
		}
	}

	public Class getColumnClass(int column) {
		return Object.class;

	}

	public int getRowCount() {
		try {
			return c.getParticipants().getAllParticipants().size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getColumnCount() {
		return 8;
	}

	public void updateData() {
		fireTableDataChanged();
		System.err.println("UPDATING THE DTATA2");
		Vector v = c.getParticipants().getAllParticipants();
		for (int i = 0; i < v.size(); i++) {
			Participant p = (Participant) v.elementAt(i);
			System.err.println("SETTING THE DATA2: " + p.getUsername() + " " + p.getNumberOfChatMessagesProduced());
		}

	}

	public void updateRowDataForSingleParticipant(Participant p) {
		final int index = c.getParticipants().getAllParticipants().indexOf(p);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fireTableRowsUpdated(index, index);

			}
		});

	}

	private void doLoopUpdatingTimeSinceLastKeypress() {
		while (2 < 5) {
			try {
				Thread.sleep(1000);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						for (int i = 0; i < c.getParticipants().getAllParticipants().size(); i++) {
							fireTableCellUpdated(i, 6);
							fireTableCellUpdated(i, 7);
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class UIUpdatingThread extends Thread {

		public void run() {
			while (2 < 5) {
				try {
					Thread.sleep(1000);

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < c.getParticipants().getAllParticipants().size(); i++) {
								fireTableCellUpdated(i, 6);
								fireTableCellUpdated(i, 7);
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}