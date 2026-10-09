package taskflow;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TaskFlowApp extends JFrame {

    // ── Colors ──────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(245, 247, 250);
    private static final Color SIDEBAR_BG  = new Color(30,  41,  59);
    private static final Color ACCENT      = new Color(99, 102, 241);
    private static final Color DANGER      = new Color(239,  68,  68);
    private static final Color SUCCESS     = new Color(34, 197,  94);
    private static final Color CARD_BG     = Color.WHITE;
    private static final Color TEXT_DARK   = new Color(15,  23,  42);
    private static final Color TEXT_MUTED  = new Color(100, 116, 139);
    private static final Color HIGH_COLOR  = new Color(239,  68,  68);
    private static final Color MED_COLOR   = new Color(245, 158,  11);
    private static final Color LOW_COLOR   = new Color(34, 197,  94);

    // ── State ────────────────────────────────────────────────────────────────
    private User user;
    private String currentFilter = "All";

    // ── UI Components ────────────────────────────────────────────────────────
    private JPanel taskListPanel;
    private JLabel filterLabel;
    private JLabel taskCountLabel;

    // ── Constructor ──────────────────────────────────────────────────────────
    public TaskFlowApp() {
        user = new User("User");
        setupFrame();
        buildUI();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("TaskFlow - Personal Task Manager");
        setSize(1000, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);
    }

    // ── Main UI ──────────────────────────────────────────────────────────────
    private void buildUI() {
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainPanel(), BorderLayout.CENTER);
    }

    // ── Sidebar ──────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        // App title
        JLabel title = new JLabel("TaskFlow");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(title);

        JLabel subtitle = new JLabel("Task Manager");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(148, 163, 184));
        subtitle.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(32));

        // Filter label
        JLabel filterHeading = new JLabel("FILTER BY");
        filterHeading.setFont(new Font("Segoe UI", Font.BOLD, 10));
        filterHeading.setForeground(new Color(100, 116, 139));
        filterHeading.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(filterHeading);
        sidebar.add(Box.createVerticalStrut(8));

        // Filter buttons
        String[] filters = {"All", "High", "Medium", "Low", "Work", "Personal", "School"};
        for (String f : filters) {
            sidebar.add(makeSidebarBtn(f));
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());

        // Add Task button at bottom
        JButton addBtn = new JButton("+ Add Task");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setBackground(ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addBtn.setAlignmentX(LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> showAddTaskDialog());
        sidebar.add(addBtn);

        return sidebar;
    }

    private JButton makeSidebarBtn(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(e -> applyFilter(label));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(51, 65, 85));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SIDEBAR_BG);
            }
        });
        return btn;
    }

    // ── Main Panel ───────────────────────────────────────────────────────────
    private JPanel buildMainPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(24, 24, 12, 24));

        filterLabel = new JLabel("All Tasks");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        filterLabel.setForeground(TEXT_DARK);

        taskCountLabel = new JLabel("0 tasks");
        taskCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taskCountLabel.setForeground(TEXT_MUTED);

        JPanel headerLeft = new JPanel();
        headerLeft.setBackground(BG);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.add(filterLabel);
        headerLeft.add(taskCountLabel);

        header.add(headerLeft, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);

        // Task list scroll pane
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(BG);
        taskListPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 24, 24));

        JScrollPane scroll = new JScrollPane(taskListPanel);
        scroll.setBackground(BG);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        main.add(scroll, BorderLayout.CENTER);

        refreshTaskList();
        return main;
    }

    // ── Task Card ────────────────────────────────────────────────────────────
    private JPanel buildTaskCard(Task task, int index) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Priority stripe
        JPanel stripe = new JPanel();
        stripe.setPreferredSize(new Dimension(5, 0));
        stripe.setBackground(priorityColor(task.getPriority()));
        card.add(stripe, BorderLayout.WEST);

        // Center info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(CARD_BG);

        JLabel titleLbl = new JLabel(task.getTitle());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(task.getStatus().equals("Done") ? TEXT_MUTED : TEXT_DARK);

        JLabel metaLbl = new JLabel(task.getCategory() + "  •  Due: " + task.getDueDate() + "  •  " + task.getPriority() + " Priority");
        metaLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        metaLbl.setForeground(TEXT_MUTED);

        // Status badge
        JLabel statusBadge = new JLabel(task.getStatus());
        statusBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        statusBadge.setForeground(task.getStatus().equals("Done") ? SUCCESS : ACCENT);
        statusBadge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        statusBadge.setOpaque(true);
        statusBadge.setBackground(task.getStatus().equals("Done")
            ? new Color(220, 252, 231) : new Color(238, 242, 255));

        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        badgeWrap.setBackground(CARD_BG);
        badgeWrap.add(statusBadge);

        info.add(titleLbl);
        info.add(Box.createVerticalStrut(3));
        info.add(metaLbl);
        info.add(badgeWrap);
        card.add(info, BorderLayout.CENTER);

        // Action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setBackground(CARD_BG);

        JButton detailBtn = makeIconBtn("Details", new Color(99, 102, 241));
        JButton doneBtn   = makeIconBtn("Done", SUCCESS);
        JButton delBtn    = makeIconBtn("Delete", DANGER);

        detailBtn.addActionListener(e -> showDetailsDialog(task));
        doneBtn.addActionListener(e -> markDone(index));
        delBtn.addActionListener(e -> deleteTask(index));

        if (task.getStatus().equals("Done")) {
            doneBtn.setEnabled(false);
            doneBtn.setBackground(new Color(200, 200, 200));
        }

        actions.add(detailBtn);
        actions.add(doneBtn);
        actions.add(delBtn);
        card.add(actions, BorderLayout.EAST);

        return card;
    }

    private JButton makeIconBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(70, 28));
        return btn;
    }

    private Color priorityColor(String priority) {
        return switch (priority) {
            case "High"   -> HIGH_COLOR;
            case "Medium" -> MED_COLOR;
            default       -> LOW_COLOR;
        };
    }

    // ── Refresh List ─────────────────────────────────────────────────────────
    private void refreshTaskList() {
        taskListPanel.removeAll();

        ArrayList<Task> tasks = getFilteredTasks();

        if (tasks.isEmpty()) {
            JLabel empty = new JLabel("No tasks found. Click '+ Add Task' to get started.");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setForeground(TEXT_MUTED);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            taskListPanel.add(Box.createVerticalStrut(60));
            taskListPanel.add(empty);
        } else {
            TaskManager tm = user.getTaskManager();
            ArrayList<Task> allTasks = tm.getAllTasks();

            for (Task task : tasks) {
                int realIndex = allTasks.indexOf(task);
                taskListPanel.add(buildTaskCard(task, realIndex));
                taskListPanel.add(Box.createVerticalStrut(10));
            }
        }

        int count = tasks.size();
        taskCountLabel.setText(count + (count == 1 ? " task" : " tasks"));
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private ArrayList<Task> getFilteredTasks() {
        TaskManager tm = user.getTaskManager();
        return switch (currentFilter) {
            case "High", "Medium", "Low"          -> tm.filterByPriority(currentFilter);
            case "Work", "Personal", "School"     -> tm.filterByCategory(currentFilter);
            default                               -> tm.getAllTasks();
        };
    }

    private void applyFilter(String filter) {
        currentFilter = filter;
        filterLabel.setText(filter.equals("All") ? "All Tasks" : filter + " Tasks");
        refreshTaskList();
    }

    // ── Add Task Dialog ──────────────────────────────────────────────────────
    private void showAddTaskDialog() {
        JDialog dialog = new JDialog(this, "Add New Task", true);
        dialog.setSize(440, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(BG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields
        JTextField titleField    = styledField();
        JTextField dueDateField  = styledField();
        dueDateField.setText("YYYY-MM-DD");
        JComboBox<String> priorityBox  = styledCombo(new String[]{"High", "Medium", "Low"});
        JComboBox<String> categoryBox  = styledCombo(new String[]{"Work", "Personal", "School"});
        JTextField extraField1   = styledField();
        JTextField extraField2   = styledField();
        JLabel extra1Label       = styledLabel("Department:");
        JLabel extra2Label       = styledLabel("Project Name:");

        // Update extra fields based on category
        categoryBox.addActionListener(e -> {
            String cat = (String) categoryBox.getSelectedItem();
            if ("Work".equals(cat)) {
                extra1Label.setText("Department:");
                extra2Label.setText("Project Name:");
            } else if ("Personal".equals(cat)) {
                extra1Label.setText("Location:");
                extra2Label.setText("Reminder Note:");
            } else {
                extra1Label.setText("Subject:");
                extra2Label.setText("Professor:");
            }
        });

        int row = 0;
        addFormRow(form, gbc, row++, styledLabel("Task Title:"),    titleField);
        addFormRow(form, gbc, row++, styledLabel("Due Date:"),      dueDateField);
        addFormRow(form, gbc, row++, styledLabel("Priority:"),      priorityBox);
        addFormRow(form, gbc, row++, styledLabel("Category:"),      categoryBox);
        addFormRow(form, gbc, row++, extra1Label,                   extraField1);
        addFormRow(form, gbc, row,   extra2Label,                   extraField2);

        dialog.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnRow.setBackground(BG);

        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancel.addActionListener(e -> dialog.dispose());

        JButton save = new JButton("Add Task");
        save.setFont(new Font("Segoe UI", Font.BOLD, 13));
        save.setBackground(ACCENT);
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.setBorderPainted(false);
        save.addActionListener(e -> {
            try {
                String title    = titleField.getText().trim();
                String dueDate  = dueDateField.getText().trim();
                String priority = (String) priorityBox.getSelectedItem();
                String category = (String) categoryBox.getSelectedItem();
                String extra1   = extraField1.getText().trim();
                String extra2   = extraField2.getText().trim();

                if (title.isEmpty() || dueDate.isEmpty() || extra1.isEmpty() || extra2.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required.");
                }

                Task task = switch (category) {
                    case "Work"     -> new WorkTask(title, dueDate, priority, extra1, extra2);
                    case "Personal" -> new PersonalTask(title, dueDate, priority, extra1, extra2);
                    default         -> new SchoolTask(title, dueDate, priority, extra1, extra2);
                };

                user.getTaskManager().addTask(task);
                refreshTaskList();
                dialog.dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRow.add(cancel);
        btnRow.add(save);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── Details Dialog ───────────────────────────────────────────────────────
    private void showDetailsDialog(Task task) {
        JDialog dialog = new JDialog(this, "Task Details", true);
        dialog.setSize(360, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_BG);
        dialog.setLayout(new BorderLayout());

        JTextArea details = new JTextArea(task.getDetails());
        details.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        details.setEditable(false);
        details.setBackground(CARD_BG);
        details.setForeground(TEXT_DARK);
        details.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton close = new JButton("Close");
        close.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        close.addActionListener(e -> dialog.dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnRow.setBackground(CARD_BG);
        btnRow.add(close);

        dialog.add(details, BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── Actions ──────────────────────────────────────────────────────────────
    private void markDone(int index) {
        try {
            user.getTaskManager().markTaskDone(index);
            refreshTaskList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask(int index) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this task?", "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                user.getTaskManager().deleteTask(index);
                refreshTaskList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Form Helpers ─────────────────────────────────────────────────────────
    private void addFormRow(JPanel form, GridBagConstraints gbc, int row, JComponent label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(field, gbc);
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    private JTextField styledField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return combo;
    }

    // ── Entry Point ──────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(TaskFlowApp::new);
    }
}
