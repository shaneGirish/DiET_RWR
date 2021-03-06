/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import javax.swing.SwingUtilities;

/**
 *
 * @author gj
 */
public class JStatusBar extends javax.swing.JPanel {

	JExperimentManagerMainFrame jemf;

	/**
	 * Creates new form JStatusBar
	 */
	public JStatusBar() {
		initComponents();
	}

	/**
	 * Creates new form JStatusBar
	 */
	public JStatusBar(JExperimentManagerMainFrame jemf) {
		this.jemf = jemf;
		initComponents();
	}

	public void setNumberOfParticipants(final String numberOfP) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jLabel2.setText(numberOfP);
			}
		});
	}

	public void setNumberOfDisconnects(final String numberOfDisconnects) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jLabel4.setText(numberOfDisconnects);
			}
		});
	}

	public void setMaxProcessingTime(final String maxProcessingTime) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jLabel6.setText(maxProcessingTime);
			}
		});
	}

	public void setOverrun(final String maxProcessingTime) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jLabel12.setText(maxProcessingTime);
			}
		});
	}

	public void setMessagesSent(final String messagesSent) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jLabel8.setText(messagesSent);
			}
		});
	}

	public void setMessagesReceived(final String messagesReceived) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jLabel10.setText(messagesReceived);
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jSeparator2 = new javax.swing.JSeparator();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jSeparator3 = new javax.swing.JSeparator();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jSeparator4 = new javax.swing.JSeparator();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jSeparator5 = new javax.swing.JSeparator();
		jLabel12 = new javax.swing.JLabel();

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel1.setText("No. participants:");

		jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

		jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel2.setText("0");

		jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel3.setText("Disconnects:");

		jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel4.setText("0");

		jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

		jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel5.setText("Max Processing time:");

		jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel6.setText("0");

		jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

		jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel7.setText("Msgs sent:");

		jLabel8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel8.setText("0");

		jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

		jLabel9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel9.setText("Msgs recvd:");

		jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel10.setText("0");

		jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel11.setText("Overruns:");

		jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

		jLabel12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		jLabel12.setText("0");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15,
								javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel4)
				.addGap(18, 18, 18)
				.addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18)
				.addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12)
				.addGap(26, 26, 26)
				.addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE).addGap(12, 12, 12).addComponent(jLabel7)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 48,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel9)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel10,
						javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSeparator2)
				.addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addGap(0, 0, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel7).addComponent(jLabel8))
								.addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.PREFERRED_SIZE, 14,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel9).addComponent(jLabel10))))
				.addComponent(jSeparator5)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3)
						.addComponent(jLabel4))
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5)
						.addComponent(jLabel6))
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
						.addComponent(jLabel2))
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11)
						.addComponent(jLabel12)));
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JSeparator jSeparator4;
	private javax.swing.JSeparator jSeparator5;
	// End of variables declaration//GEN-END:variables
}
