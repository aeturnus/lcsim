package lcsim.pkg.gui;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingConstants;

import lcsim.pkg.*;
import lcsim.pkg.Package;

public class PackageSelector extends JPanel
{
    private PackageManager pacman = null;
    private PackageManagerWindow pacmanWindow = null;
    private Package selectedPackage = null;
    
    JPanel corePanel;
    JLabel coreLabel;
    JScrollPane coreScroll;
    JList<CorePackage> coreChooser;
    DefaultListModel<CorePackage> coreModel;
    ListSelectionListener coreListener;
    
    JPanel devicePanel;
    JLabel deviceLabel;
    JScrollPane deviceScroll;
    JList<DevicePackage> deviceChooser;
    DefaultListModel<DevicePackage> deviceModel;
    ListSelectionListener deviceListener;
    
    JPanel loaderPanel;
    JLabel loaderLabel;
    JScrollPane loaderScroll;
    JList<CodeLoaderPackage> loaderChooser;
    DefaultListModel<CodeLoaderPackage> loaderModel;
    ListSelectionListener loaderListener;
    
    JPanel debuggerPanel;
    JLabel debuggerLabel;
    JScrollPane debuggerScroll;
    JList<DebuggerPackage> debuggerChooser;
    DefaultListModel<DebuggerPackage> debuggerModel;
    ListSelectionListener debuggerListener;
    
    JPanel loadedPanel;
    JLabel loadedLabel;
    JScrollPane loadedScroll;
    JList<Package> loadedChooser;
    DefaultListModel<Package> loadedModel;
    ListSelectionListener loadedListener;
    
    public PackageSelector(PackageManagerWindow packageManagerWindow)
    {
        // TODO Auto-generated constructor stub
        pacmanWindow = packageManagerWindow;
        pacman = pacmanWindow.getPackageManager();
        
        //this.setLayout(new GridLayout(4,0,0,0));
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        //
        coreLabel = new JLabel(PackageType.CORE.toString());
        coreLabel.setVisible(true);
        this.add(coreLabel);
        
        coreScroll = new JScrollPane();
        this.add(coreScroll);
        
        coreModel = new DefaultListModel();
        coreChooser = new JList<CorePackage>(coreModel);
        coreChooser.setVisible(true);
        coreChooser.setCellRenderer(new PackageListCellRenderer());
        coreChooser.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        coreScroll.setViewportView(coreChooser);
        coreListener = new ListSelectionListener() {public void valueChanged(ListSelectionEvent e){coreSelected(e);}};
        coreChooser.addListSelectionListener(coreListener);

        //
        deviceLabel = new JLabel(PackageType.DEVICE.toString());
        deviceLabel.setVisible(true);
        this.add(deviceLabel);
        
        deviceScroll = new JScrollPane();
        this.add(deviceScroll);
        
        deviceModel = new DefaultListModel();
        deviceChooser = new JList<DevicePackage>(deviceModel);
        deviceChooser.setVisible(true);
        deviceChooser.setCellRenderer(new PackageListCellRenderer());
        deviceChooser.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        deviceScroll.setViewportView(deviceChooser);
        deviceListener = new ListSelectionListener() {public void valueChanged(ListSelectionEvent e){deviceSelected(e);}};
        deviceChooser.addListSelectionListener(deviceListener);
        //deviceChooser.addListSelectionListener(onSelect);
        
        //
        loaderLabel = new JLabel(PackageType.CODELOADER.toString());
        loaderLabel.setVisible(true);
        this.add(loaderLabel);
        
        loaderScroll = new JScrollPane();
        this.add(loaderScroll);
        
        loaderModel = new DefaultListModel();
        loaderChooser = new JList<CodeLoaderPackage>(loaderModel);
        loaderChooser.setVisible(true);
        loaderChooser.setCellRenderer(new PackageListCellRenderer());
        loaderChooser.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        loaderScroll.setViewportView(loaderChooser);
        loaderListener = new ListSelectionListener() {public void valueChanged(ListSelectionEvent e){loaderSelected(e);}};
        loaderChooser.addListSelectionListener(loaderListener);
        
        //
        debuggerLabel = new JLabel(PackageType.DEBUGGER.toString());
        debuggerLabel.setVisible(true);
        this.add(debuggerLabel);
        
        debuggerScroll = new JScrollPane();
        this.add(debuggerScroll);
        
        debuggerModel = new DefaultListModel();
        debuggerChooser = new JList<DebuggerPackage>(debuggerModel);
        debuggerChooser.setVisible(true);
        debuggerChooser.setCellRenderer(new PackageListCellRenderer());
        debuggerChooser.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        debuggerScroll.setViewportView(debuggerChooser);
        debuggerListener = new ListSelectionListener() {public void valueChanged(ListSelectionEvent e){debuggerSelected(e);}};
        debuggerChooser.addListSelectionListener(debuggerListener);
        
        
        //
        loadedLabel = new JLabel("Loaded packages");
        loadedLabel.setVisible(true);
        this.add(loadedLabel);
        
        loadedScroll = new JScrollPane();
        this.add(loadedScroll);
        
        loadedModel = new DefaultListModel();
        loadedChooser = new JList<Package>(loadedModel);
        loadedChooser.setVisible(true);
        loadedChooser.setCellRenderer(new PackageListCellRenderer());
        loadedChooser.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);;
        loadedScroll.setViewportView(loadedChooser);
        loadedListener = new ListSelectionListener() {public void valueChanged(ListSelectionEvent e){loadedSelected(e);}};
        loadedChooser.addListSelectionListener(loadedListener);
        //loadedChooser.addListSelectionListener(onSelect);
        
        update();
    }
    
    public void update()
    {
        coreModel.clear();
        CorePackage[] cores = pacman.getCores();
        for(int i = 0; i < cores.length; i++)
        {
            coreModel.addElement(cores[i]);
        }
        
        deviceModel.clear();
        DevicePackage[] devices= pacman.getDevices();
        for(int i = 0; i < devices.length; i++)
        {
            deviceModel.addElement(devices[i]);
        }
        
        loaderModel.clear();
        CodeLoaderPackage[] loaders= pacman.getCodeLoaders();
        for(int i = 0; i < loaders.length; i++)
        {
            loaderModel.addElement(loaders[i]);
        }
        
        debuggerModel.clear();
        DebuggerPackage[] debuggers= pacman.getDebuggers();
        for(int i = 0; i < debuggers.length; i++)
        {
            debuggerModel.addElement(debuggers[i]);
        }
        
        loadedModel.clear();
        Package[] loadeds = pacman.getLoadedPackages();
        for(int i = 0; i < loadeds.length; i++)
        {
            loadedModel.addElement(loadeds[i]);
        }
        
        //Disable selection of other selections without a core
        if(!pacman.getSystem().hasCore())
        {
            coreChooser.setEnabled(true);
            coreChooser.setVisible(true);
            
            if(deviceChooser.isVisible())
            {
                deviceLabel.setText("Device (load a Core first)");
            }
            deviceChooser.setEnabled(false);
            deviceChooser.setVisible(false);
            
            loaderChooser.setEnabled(true);
            loaderChooser.setVisible(true);
            
            if(debuggerChooser.isVisible())
            {
                debuggerLabel.setText("Debugger (load a Core first)");
            }
            debuggerChooser.setEnabled(false);
            debuggerChooser.setVisible(false);

            
            loadedChooser.setEnabled(true);
            loadedChooser.setVisible(true);
        }
        else
        {
            coreChooser.setEnabled(true);
            coreChooser.setVisible(true);
            
            if(!deviceChooser.isVisible())
            {
                deviceLabel.setText("Device");
            }
            deviceChooser.setEnabled(true);
            deviceChooser.setVisible(true);
            
            loaderChooser.setEnabled(true);
            loaderChooser.setVisible(true);
            
            if(!debuggerChooser.isVisible())
            {
                debuggerLabel.setText("Debugger");
            }
            debuggerChooser.setEnabled(true);
            debuggerChooser.setVisible(true);
            
            loadedChooser.setEnabled(true);
            loadedChooser.setVisible(true);
        }
    }
    
    
    
    Package getSelectedPackage()
    {
        return selectedPackage;
    }
    
    private void coreSelected(ListSelectionEvent e)
    {
        selectedPackage = null;
        
        //coreChooser.clearSelection();
        deviceChooser.clearSelection();
        loaderChooser.clearSelection();
        debuggerChooser.clearSelection();
        loadedChooser.clearSelection();
        
        selectedPackage = coreChooser.getSelectedValue();
        pacmanWindow.handleSelectorSelection();
    }
    private void deviceSelected(ListSelectionEvent e)
    {
        selectedPackage = null;
        
        coreChooser.clearSelection();
        loaderChooser.clearSelection();
        //deviceChooser.clearSelection();
        debuggerChooser.clearSelection();
        loadedChooser.clearSelection();
        
        selectedPackage = deviceChooser.getSelectedValue();
        pacmanWindow.handleSelectorSelection();
    }
    private void loaderSelected(ListSelectionEvent e)
    {
        selectedPackage = null;
        
        coreChooser.clearSelection();
        deviceChooser.clearSelection();
        //loaderChooser.clearSelection();
        debuggerChooser.clearSelection();
        loadedChooser.clearSelection();
        
        selectedPackage = loaderChooser.getSelectedValue();
        pacmanWindow.handleSelectorSelection();
    }
    private void debuggerSelected(ListSelectionEvent e)
    {
        selectedPackage = null;
        
        coreChooser.clearSelection();
        deviceChooser.clearSelection();
        loaderChooser.clearSelection();
        //debuggerChooser.clearSelection();
        loadedChooser.clearSelection();
        
        selectedPackage = debuggerChooser.getSelectedValue();
        pacmanWindow.handleSelectorSelection();
    }
    private void loadedSelected(ListSelectionEvent e)
    {
        selectedPackage = null;
        
        coreChooser.clearSelection();
        deviceChooser.clearSelection();
        loaderChooser.clearSelection();
        debuggerChooser.clearSelection();
        //loadedChooser.clearSelection();
        
        selectedPackage = loadedChooser.getSelectedValue();
        pacmanWindow.handleSelectorSelection();
    }

}
