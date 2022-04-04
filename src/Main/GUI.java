package Main;

import Simulation.SalonSimulation;
import Simulation.SimCore;
import Statistics.AverageSizeOfQueueForReplications;
import Statistics.AverageStatistic;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Queue;

public class GUI extends JDialog implements ISimDelegate {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonExit;
    private JButton simulateButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JTextField replications;
    private JTable receptionistsTable;
    private JTable hairstylistsTable;
    private JTable cosmeticiansTable;
    private JTable customersTable;
    private JTable receptionQueueTable;
    private JTable hairstylingQueueTable;
    private JTable payQueueTable;
    private JTable makeupQueueTable;
    private JLabel simulationTime;
    private JSlider slider1;
    private JLabel sliderValue;
    private JTextField receptionists;
    private JTextField hairstylists;
    private JTextField cosmeticians;
    private JLabel hairAndMakeUpSize;
    private JTabbedPane tabbedPane;
    private JPanel simulationPanel;
    private JPanel replicationsPanel;
    private JPanel graphPanel;
    private JTable replicationsTable;

    private String[] employeeColumnsNames = {"Type" , "Occupied", "Worked time"};
    private String[] customerColumnsNames = {"Id", "Arrival time" , "Choice", "Position"};
    private String[] statisticsColumnsNames = {"Name", "Value"};

    private SalonSimulation simulation;
    private GUI gui;

    public GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Beauty Salon Simulation");

        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (dimension.getWidth() / 2 - getWidth() / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);

        this.gui = this;

        final Thread[] thread = new Thread[1];

        setUpGraph();

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });


        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread[0] = new Thread(() -> {
                    SalonSimulation simulation;
                    int selectedIndex = tabbedPane.getSelectedIndex();

                    try {
                        if (selectedIndex == 0) {
                            simulation = new SalonSimulation(Integer.parseInt(replications.getText()), receptionists.getText(), hairstylists.getText(), cosmeticians.getText());
                            simulation.setCurrentMode(1);
                            simulation.setRefreshTime(10);
                            simulation.setSpeed(10);
                            simulation.registerDelegate(gui);
                            simulation.simulate();
                        } else if (selectedIndex == 1) {
                            simulation = new SalonSimulation(Integer.parseInt(replications.getText()), receptionists.getText(), hairstylists.getText(), cosmeticians.getText());
                            simulation.setCurrentMode(2);
                            simulation.setSpeed(100);
                            simulation.registerDelegate(gui);
                            simulation.simulate();
                        } else {
                            for (int i = 1; i <= 10; i++) {
                                simulation = new SalonSimulation(Integer.parseInt(replications.getText()), receptionists.getText(), i + "", cosmeticians.getText());
                                simulation.setCurrentMode(3);
                                simulation.setSpeed(100);
                                simulation.registerDelegate(gui);
                                simulation.simulate();
                            }
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
                thread[0].start();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulation.setPause();
                if (pauseButton.getText().equals("Pause")) {
                    pauseButton.setText("Resume");
                } else {
                    pauseButton.setText("Pause");
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulation.setStop();
            }
        });


        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();
                    if (value == 100) {
                        sliderValue.setText("max");
                    } else {
                        sliderValue.setText(value + "");
                    }
                    slider1.setValue(value);
                    if (simulation != null) {
                        simulation.setSpeed(slider.getValue());
                    }
                }
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
                if (simulation != null) {
                    int selectedIndex = tabbedPane.getSelectedIndex();
                    if (selectedIndex == 0) {
                        simulation.setCurrentMode(1);
                        simulation.setRefreshTime(10);
                        simulation.setSpeed(10);
                    } else if (selectedIndex == 1) {
                        simulation.setCurrentMode(2);
                        simulation.setSpeed(100);
                    } else {
                        simulation.setCurrentMode(3);
                        simulation.setSpeed(100);
                    }
                }
            }
        });
    }

    private void onCancel() {
        dispose();
        System.exit(0);
    }

    @Override
    public void refresh(SimCore simulation) {
        this.simulation = (SalonSimulation) simulation;

        if (tabbedPane.getSelectedIndex() == 0) {
            setUpEmployeeTable(this.simulation.getReceptionists(), this.receptionistsTable);
            setUpEmployeeTable(this.simulation.getHairstylists(), this.hairstylistsTable);
            setUpEmployeeTable(this.simulation.getCosmeticians(), this.cosmeticiansTable);

            setUpQueueTable(this.simulation.getReceptionQueue(), this.receptionQueueTable);
            setUpQueueTable(this.simulation.getHairstylingQueue(), this.hairstylingQueueTable);
            setUpQueueTable(this.simulation.getMakeupQueue(), this.makeupQueueTable);
            setUpQueueTable(this.simulation.getPayQueue(), this.payQueueTable);

            setUpCustomerTable(this.simulation.getCustomersInSalon(), this.customersTable);

            this.hairAndMakeUpSize.setText("Hairstyling and Makeup Queue size : " + (this.simulation.getHairstylingQueue().size() + this.simulation.getMakeupQueue().size()));
            this.simulationTime.setText("Simulation Time : " + this.simulation.getSimulationTime());
        } else if (tabbedPane.getSelectedIndex() == 1) {
            setUpReplicationsTable();
        } else {
            updateGraph(this.simulation.getAverageSizeOfQueueForReplications(), this.simulation.getNumberOfHairstylists());
        }
    }

    private void setUpGraph() {

    }

    private void updateGraph(AverageSizeOfQueueForReplications averageSizeOfQueueForReplications, int numberOfHairstylists) {

    }

    private void setUpReplicationsTable() {
        receptionistsTable.clearSelection();
        ArrayList<AverageStatistic> stats = this.simulation.getAverageStatistics();
        String[][] data = new String[stats.size() * 2 - 1][statisticsColumnsNames.length];
        for (int i = 0; i < stats.size(); i++) {
            data[i][0] = stats.get(i).getStatisticName() + "";
            data[i][1] = stats.get(i).getTime() / stats.get(i).getCount() + "";
        }
        for (int i = 0; i < stats.size() - 1; i++) {
            data[i + stats.size()][0] = stats.get(i).getStatisticName() + "WithCooling";
            data[i + stats.size()][1] = stats.get(i).getTimeCooling() / stats.get(i).getCountCooling() + "";
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, statisticsColumnsNames);
        setTableModel(replicationsTable, tableModel);
    }

    private void setUpCustomerTable(ArrayList<Customer> customers, JTable table) {
        String[][] data = new String[customers.size()][customerColumnsNames.length];
        for (int i = 0; i < customers.size(); i++) {
            data[i][0] = customers.get(i).getNum() + "";
            data[i][1] = customers.get(i).getArrivalTime() + "";
            data[i][2] = customers.get(i).getOrder() + "";
            data[i][3] = customers.get(i).getStatus() + "";
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, customerColumnsNames);
        setTableModel(table, tableModel);
    }

    private void setTableModel(JTable table, DefaultTableModel tableModel) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                table.setModel(tableModel);
            }
        });
    }

    private void setUpEmployeeTable(ArrayList<Employee> employees, JTable table) {
        String[][] data = new String[employees.size()][employeeColumnsNames.length];
        for (int i = 0; i < employees.size(); i++) {
            data[i][0] = employees.get(i).getType() + "";
            data[i][1] = employees.get(i).getOccupied() + "";
            data[i][2] = employees.get(i).getTimeWorked() + "";
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, employeeColumnsNames);
        setTableModel(table, tableModel);
    }

    private void setUpQueueTable(Queue<Customer> queue, JTable table) {
        String[][] data = new String[queue.size()][customerColumnsNames.length];
        int count = 0;
        for (Customer customer : queue) {
            data[count][0] = customer.getNum() + "";
            data[count][1] = customer.getArrivalTime() + "";
            data[count][2] = customer.getOrder() + "";
            data[count][3] = customer.getStatus() + "";
            count++;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, customerColumnsNames);
        setTableModel(table, tableModel);
    }

}
