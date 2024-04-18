package instructionfilemanipulator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Nathaniel
 */
public class MainWindow extends JFrame{
    
    public MainWindow(){
        last_directory = "";
        
        this.getContentPane().removeAll();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx=DEFAULT_PAD;
        c.ipady=DEFAULT_PAD;
        
        this.add(new JLabel(""), c);
        
        JPanel open_file_bundle = new JPanel(new GridBagLayout());
        JPanel image_file_bundle = new JPanel(new GridBagLayout());
        JPanel if_file_bundle = new JPanel(new GridBagLayout());
        
        open_file_button = new JButton("Browse");
        open_file_button.addActionListener((ActionEvent e) -> {
            openFile();
        });
        open_file_bundle.add(open_file_button, c);
        
        save_image_button = new JButton("Browse");
        save_image_button.addActionListener((ActionEvent e) -> {
            imageBrowseClicked();
        });
        image_file_bundle.add(save_image_button, c);
        
        save_if_button = new JButton("Browse");
        save_if_button.addActionListener((ActionEvent e) -> {
            ifBrowseClicked();
        });
        if_file_bundle.add(save_if_button, c);
        
        c.gridx = 1;
        c.weightx = 1;
        c.ipadx = FIELD_PAD;
        
        open_file_field = new JTextField("No File.");
        open_file_bundle.add(open_file_field, c);
        
        save_image_field = new JTextField("No File.");
        image_file_bundle.add(save_image_field, c);
        
        save_if_field = new JTextField("No File.");
        if_file_bundle.add(save_if_field, c);
        
        c.gridy = 1;
        c.ipadx=DEFAULT_PAD;
        this.add(new JLabel("Initial Instruction File:"), c);
        c.gridy = 2;
        this.add(open_file_bundle, c);
        
        c.gridy = 3;
        save_image_option = new JCheckBox("Export image data", true);
        save_image_option.addActionListener((ActionEvent e) -> {
            imageOptionClicked();
        });
        this.add(save_image_option, c);
        c.gridy = 4;
        this.add(image_file_bundle, c);
        
        c.gridy = 5;
        save_if_option = new JCheckBox("Export instruction file with same shape", true);
        save_if_option.addActionListener((ActionEvent e) -> {
            ifOptionClicked();
        });
        this.add(save_if_option, c);
        c.gridy = 6;
        this.add(if_file_bundle, c);
        
        c.gridy = 7;
        c.fill = GridBagConstraints.NONE;
        run_button = new JButton("Run");
        run_button.addActionListener((ActionEvent e) -> {
            runButtonClicked();
        });
        this.add(run_button, c);
        c.gridy = 8;
        c.weighty = 1;
        this.add(new JLabel(""), c);
        
        this.pack();
        this.setSize(600, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Instruction File Converter");
    }
    
    private void openFile(){
        JFileChooser jfc = new JFileChooser();
        if(!last_directory.equals("")){
            File dir = new File(last_directory);
            if(dir.isDirectory()){
                jfc.setCurrentDirectory(dir);
            }
        }
        jfc.setDialogTitle("Select Instruction File to read:");
        int result = jfc.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            File f = jfc.getSelectedFile();
            File d = jfc.getCurrentDirectory();
            String filepath = f.getAbsolutePath();
            last_directory = d.getAbsolutePath();
            open_file_field.setText(filepath);
        }
    }
    
    private void imageOptionClicked(){
        boolean state = save_image_option.isSelected();
        save_image_button.setEnabled(state);
        save_image_field.setEnabled(state);
    }
    
    private void imageBrowseClicked(){
        JFileChooser jfc = new JFileChooser();
        if(!last_directory.equals("")){
            File dir = new File(last_directory);
            if(dir.isDirectory()){
                jfc.setCurrentDirectory(dir);
            }
        }
        jfc.setDialogTitle("Select image file destination:");
        int result = jfc.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            File f = jfc.getSelectedFile();
            File d = jfc.getCurrentDirectory();
            String filepath = f.getAbsolutePath();
            last_directory = d.getAbsolutePath();
            save_image_field.setText(filepath);
        }
    }
    
    private void ifOptionClicked(){
        boolean state = save_if_option.isSelected();
        save_if_button.setEnabled(state);
        save_if_field.setEnabled(state);
    }
    
    private void ifBrowseClicked(){
        JFileChooser jfc = new JFileChooser();
        if(!last_directory.equals("")){
            File dir = new File(last_directory);
            if(dir.isDirectory()){
                jfc.setCurrentDirectory(dir);
            }
        }
        jfc.setDialogTitle("Select Instruction File destination:");
        int result = jfc.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            File f = jfc.getSelectedFile();
            File d = jfc.getCurrentDirectory();
            String filepath = f.getAbsolutePath();
            last_directory = d.getAbsolutePath();
            save_if_field.setText(filepath);
        }
    }
    
    private void runButtonClicked(){
        File source_file = new File(open_file_field.getText().strip());
        
        if(source_file.exists()){
            try{
                boolean image_success = false;
                boolean if_success = false;
                Manipulator mn = new Manipulator(open_file_field.getText().strip());
                if(save_image_option.isSelected()){
                    try{
                        mn.export_image(open_file_field.getText().strip(), save_image_field.getText().strip());
                        image_success = true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
                if(save_if_option.isSelected()){
                    try{
                        mn.export_if(open_file_field.getText().strip(), save_if_field.getText().strip());
                        if_success = true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
                
                String message = "";
                
                if(!save_image_option.isSelected() && !save_if_option.isSelected()){
                    message = "File Loaded without issue.\nNo conversion operations selected.";
                }
                else{
                    if(save_image_option.isSelected()){
                        if(image_success){
                            message = message + "\nImage exported successfully.";
                        }
                        else{
                            message = message + "\nImage export encountered an error.";
                        }
                    }
                    if(save_if_option.isSelected()){
                        if(if_success){
                            message = message + "\nInstruction File converted successfully.";
                        }
                        else{
                            message = message + "\nInstruction File conversion encountered an error.";
                        }
                    }
                }
                JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);
                
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "File \"" + source_file.getName() + "\" could not be found.", "File not found", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private String last_directory;
    
    private JButton open_file_button;
    private JTextField open_file_field;
    
    private JCheckBox save_image_option;
    private JButton save_image_button;
    private JTextField save_image_field;
    
    private JCheckBox save_if_option;
    private JButton save_if_button;
    private JTextField save_if_field;
    
    private JButton run_button;
    
    private static final int FIELD_PAD = 50;
    private static final int DEFAULT_PAD = 5;
}
