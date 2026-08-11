# Thalam FXML Components Guide

Extended reference for every FXML component used in this project (`src/**/*.fxml`).

**Scope:** 25 FXML files under `src/` (build output ignored).  
**How to use:** Look up a component → read what it does → copy an example → check which attributes we actually use and why.

---

## Table of contents

1. [Screen templates (how we structure pages)](#screen-templates)
2. [Shared / common attributes](#shared--common-attributes)
3. [Layout containers](#1-layout-containers)
4. [Controls (inputs & actions)](#2-controls)
5. [Media & helper types](#3-media--helper-types)
6. [Child property elements](#4-child-property-elements)
7. [Constraint attributes cheat sheet](#5-constraint-attributes-cheat-sheet)
8. [Quick index](#6-quick-index)

---

## Screen templates

Most app screens share one shell:

```
AnchorPane (root + stylesheet + controller)
 └─ HBox (full-bleed: AnchorPane.*Anchor = 0)
     ├─ StackPane.sidebar
     │    └─ VBox.sidebar-body (brand + nav)
     └─ VBox.main-column
          ├─ HBox.main-header (Logout)
          └─ page content (list / form / dashboard / reports)
```

| Template | Typical content | Example files |
|----------|-----------------|---------------|
| **Login** | `StackPane` root, no sidebar | `Login/Login.fxml` |
| **List page** | Title + search + `TableView` + pagination | `Clients/Clients.fxml`, `Materials/Materials.fxml` |
| **Form page** | Title + `ScrollPane` + `GridPane` + Cancel/Save footer | `Expenses/AddExpense.fxml` |
| **Popup** | Compact `VBox` root (no sidebar) | `Materials/AddMaterialPopup.fxml` |
| **Dashboard** | Stat cards + `GridPane` dash cards | `Dashboard/Dashboard.fxml` |
| **Reports** | `FlowPane` filters + chart placeholders | `Reports/Reports.fxml` |

---

## Shared / common attributes

These appear on many nodes:

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Links the node to a `@FXML` field in the controller |
| `fx:controller` | Controller class for the **root** of the FXML |
| `xmlns` / `xmlns:fx` | JavaFX / FXML namespaces (on root) |
| `stylesheets` | CSS file(s), e.g. `@AddExpense.css` |
| `styleClass` | One or more CSS classes (space-separated) |
| `prefWidth` / `prefHeight` | Preferred size |
| `minWidth` / `minHeight` | Minimum size |
| `maxWidth` / `maxHeight` | Maximum size (`Infinity` = grow freely) |
| `visible` | Show/hide visually |
| `managed` | If `false`, layout ignores the node (no empty gap) |
| `alignment` | How children / text align inside the node |
| `spacing` | Gap between children (`VBox` / `HBox`) |
| `onAction` | Event handler, e.g. `#onSave` → `onSave(...)` in controller |
| `onMouseClicked` | Click handler (used on dashboard cards) |
| `mouseTransparent` | Clicks pass through to parent (nav graphic content) |
| `mnemonicParsing` | If `false`, `_` in button text is not treated as shortcut underline |
| `focusTraversable` | If `false`, Tab key skips this control |

---

## 1. Layout containers

### 1.1 AnchorPane

**What it is:** Root layout that pins children to edges with anchors (top/right/bottom/left).

**Where we use it:** Almost every app screen root (not Login, not Material popup).

**Examples:**

```xml
<!-- Dashboard root -->
<AnchorPane prefWidth="1920.0" prefHeight="1080.0"
      minWidth="1024.0" minHeight="640.0"
      maxWidth="Infinity" maxHeight="Infinity"
      stylesheets="@dashboard.css"
      xmlns="http://javafx.com/javafx/21"
      xmlns:fx="http://javafx.com/fxml/1"
      fx:controller="Dashboard.DashboardController">
```

```xml
<!-- Full-bleed child using anchors -->
<HBox maxWidth="Infinity" maxHeight="Infinity"
      AnchorPane.topAnchor="0.0" AnchorPane.rightAnchor="0.0"
      AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0">
```

**Attributes used in this project**

| Attribute | Purpose |
|-----------|---------|
| `fx:controller` | Screen controller class |
| `xmlns` / `xmlns:fx` | Namespaces |
| `stylesheets` | Page CSS |
| `prefWidth` / `prefHeight` | Design canvas size (1920×1080) |
| `minWidth` / `minHeight` | Smallest usable window (1024×640) |
| `maxWidth` / `maxHeight` | Usually `Infinity` so it fills the stage |
| `id` | Plain CSS/id (`thalam/FXML.fxml` stub only) |

**Constraint attrs we set on children:** `AnchorPane.topAnchor`, `rightAnchor`, `bottomAnchor`, `leftAnchor` — distance from that edge (we use `0.0` for full bleed).

---

### 1.2 StackPane

**What it is:** Stacks children on top of each other (z-order). Good for overlays and layered UI.

**Where we use it:** Login root; sidebar wrapper on every shell screen; password eye-toggle wrap.

**Examples:**

```xml
<!-- Login root -->
<StackPane prefWidth="1920.0" prefHeight="1080.0"
           stylesheets="@style.css"
           fx:controller="Login.LoginController"
           fx:id="rootPane" styleClass="login-root"
           maxWidth="Infinity" maxHeight="Infinity">
```

```xml
<!-- Sidebar shell -->
<StackPane styleClass="sidebar">
    <VBox styleClass="sidebar-body" StackPane.alignment="TOP_LEFT">
        ...
    </VBox>
</StackPane>
```

```xml
<!-- Password field + eye button layered -->
<StackPane styleClass="password-wrap" alignment="CENTER_RIGHT">
    <PasswordField ... maxWidth="Infinity"/>
    <Button styleClass="password-toggle-btn" StackPane.alignment="CENTER_RIGHT">...</Button>
</StackPane>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` / `fx:controller` | Root (login) wiring |
| `stylesheets` / `styleClass` | Look & feel |
| `alignment` | Default child alignment |
| `pref/min/max Width/Height` | Size limits |
| `StackPane.alignment` *(on child)* | Per-child position (`TOP_LEFT`, `CENTER_RIGHT`) |
| `AnchorPane.*Anchor` | Only on login root in our FXML (leftover/edge pin pattern) |

---

### 1.3 VBox

**What it is:** Vertical box — children stacked top → bottom.

**Where we use it:** Everywhere: sidebar body, form pages, field blocks, dashboard cards, popup root, list page body.

**Examples:**

```xml
<!-- Popup root (no sidebar) -->
<VBox prefWidth="520.0" prefHeight="420.0"
      styleClass="material-popup"
      stylesheets="@AddMaterialPopup.css"
      fx:controller="Materials.AddMaterialPopupController">
```

```xml
<!-- Form field block inside a grid cell -->
<VBox styleClass="module-form-field-block"
      GridPane.columnIndex="0" GridPane.rowIndex="0">
    <Label text="Date" styleClass="module-form-label"/>
    <DatePicker fx:id="datePicker" .../>
</VBox>
```

```xml
<!-- Show/hide section (payment mode) -->
<VBox fx:id="paymentModeSection" styleClass="module-form-field-block"
      managed="false" visible="false" maxWidth="360">
    ...
</VBox>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Controller reference (dynamic sections, row hosts) |
| `styleClass` | CSS |
| `spacing` | Vertical gap between children |
| `alignment` | Child alignment (`CENTER`, `TOP_CENTER`, …) |
| `visible` / `managed` | Toggle sections (nav kids, conditional form parts) |
| `pref/min/max Width/Height` | Sizing |
| `onMouseClicked` | Dashboard / report card clicks |
| `HBox.hgrow` | Grow horizontally when inside an `HBox` |
| `VBox.vgrow` | Grow vertically when inside a `VBox` |
| `StackPane.alignment` | Position inside sidebar `StackPane` |
| `GridPane.columnIndex` / `rowIndex` / `columnSpan` | Grid placement |
| `GridPane.hgrow` / `vgrow` / `valignment` | Grid grow / vertical align |
| Root-only: `fx:controller`, `stylesheets`, `xmlns` | Popup root |

---

### 1.4 HBox

**What it is:** Horizontal box — children left → right.

**Where we use it:** App shell row; nav items; headers; filter bars; label + required `*`; brand row; pagination.

**Examples:**

```xml
<!-- Shell row -->
<HBox maxWidth="Infinity" maxHeight="Infinity"
      AnchorPane.topAnchor="0.0" AnchorPane.rightAnchor="0.0"
      AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0">
```

```xml
<!-- Required label pair -->
<HBox spacing="0">
    <Label text="Amount " styleClass="module-form-label"/>
    <Label text="*" styleClass="module-form-required"/>
</HBox>
```

```xml
<!-- Nav button graphic content -->
<HBox styleClass="nav-content" mouseTransparent="true">
    <ImageView .../>
    <Label text="Dashboard" styleClass="nav-label"/>
</HBox>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Rare; used when needed for code |
| `styleClass` | CSS |
| `spacing` | Horizontal gap |
| `alignment` | `CENTER_LEFT`, `CENTER_RIGHT`, `CENTER`, … |
| `mouseTransparent` | Let parent Button receive clicks |
| `prefHeight` / `minHeight` / `maxWidth` / `maxHeight` | Size |
| `AnchorPane.*Anchor` | Full-bleed shell |

---

### 1.5 BorderPane

**What it is:** Five regions: top, bottom, left, right, center.

**Where we use it:** Form + popup footers only — Cancel on left, Save on right.

**Examples:**

```xml
<BorderPane styleClass="module-form-footer" maxWidth="Infinity">
    <left>
        <Button fx:id="cancelButton" text="Cancel"
                styleClass="module-form-btn-cancel"
                mnemonicParsing="false" onAction="#onCancel"/>
    </left>
    <right>
        <Button fx:id="saveButton" text="Save"
                styleClass="module-form-btn-save"
                mnemonicParsing="false" onAction="#onSave"/>
    </right>
</BorderPane>
```

```xml
<!-- Same pattern on popup -->
<BorderPane styleClass="material-popup-footer" maxWidth="Infinity">
    <left>...</left>
    <right>...</right>
</BorderPane>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `styleClass` | Footer styling |
| `maxWidth` | Stretch across form width |

**Child properties used:** `<left>`, `<right>` only. We do **not** use `<top>`, `<center>`, or `<bottom>` in this project.

---

### 1.6 GridPane

**What it is:** Table-like layout with rows and columns for form fields / dashboard cards.

**Where we use it:** Add forms, Material popup, Dashboard card rows, bulk material headers.

**Examples:**

```xml
<!-- Two-column form -->
<GridPane styleClass="module-form-grid">
    <columnConstraints>
        <ColumnConstraints percentWidth="50" hgrow="ALWAYS"/>
        <ColumnConstraints percentWidth="50" hgrow="ALWAYS"/>
    </columnConstraints>
    <VBox styleClass="module-form-field-block"
          GridPane.columnIndex="0" GridPane.rowIndex="0">...</VBox>
    <VBox styleClass="module-form-field-block"
          GridPane.columnIndex="1" GridPane.rowIndex="0">...</VBox>
</GridPane>
```

```xml
<!-- Dashboard 45/55 split -->
<GridPane hgap="16" maxWidth="Infinity" styleClass="dash-row">
    <columnConstraints>
        <ColumnConstraints percentWidth="45" hgrow="ALWAYS" minWidth="280"/>
        <ColumnConstraints percentWidth="55" hgrow="ALWAYS" minWidth="320"/>
    </columnConstraints>
    ...
</GridPane>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Rare dynamic grids |
| `styleClass` | `module-form-grid`, `dash-row`, bulk header |
| `hgap` / `vgap` | Gaps between cells |
| `maxWidth` | Stretch |
| `visible` / `managed` | Conditional grids (if used) |

**Child properties:** `<columnConstraints>`, `<rowConstraints>` (dashboard).

---

### 1.7 ColumnConstraints

**What it is:** Defines one column’s width / grow behavior inside a `GridPane`.

**Where we use it:** Every form grid, popup, dashboard, purchase bulk header.

**Examples:**

```xml
<ColumnConstraints percentWidth="50" hgrow="ALWAYS"/>
<ColumnConstraints percentWidth="33.33" hgrow="ALWAYS"/>  <!-- Add Purchase 3-col -->
<ColumnConstraints percentWidth="45" hgrow="ALWAYS" minWidth="280"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `percentWidth` | Share of grid width (e.g. 50 / 50) |
| `hgrow` | Usually `ALWAYS` so columns expand |
| `minWidth` | Floor width (dashboard columns) |

---

### 1.8 RowConstraints

**What it is:** Defines one row’s height / grow behavior inside a `GridPane`.

**Where we use it:** Dashboard only.

**Example:**

```xml
<rowConstraints>
    <RowConstraints vgrow="NEVER"/>
</rowConstraints>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `vgrow` | `NEVER` keeps dash rows from stretching vertically |

---

### 1.9 Region

**What it is:** Empty layout node — spacer or CSS-drawn icon/placeholder (no text).

**Where we use it:** Login vertical spacers; nav caret; dashboard/report icons & chart empty boxes.

**Examples:**

```xml
<!-- Flexible spacer (pushes content) -->
<Region VBox.vgrow="ALWAYS"/>

<!-- CSS caret in sidebar -->
<Region styleClass="nav-caret"/>

<!-- Chart placeholder -->
<Region styleClass="reports-chart-empty"
        maxWidth="Infinity" minHeight="260" prefHeight="260"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `styleClass` | Visual role (`nav-caret`, `stat-icon`, chart empty, …) |
| `VBox.vgrow` | Grow as spacer |
| `prefHeight` / `minHeight` / `maxWidth` | Size for placeholders |

---

### 1.10 ScrollPane

**What it is:** Scrollable viewport around content that may overflow.

**Where we use it:** Form bodies, Dashboard content, Reports charts area.

**Examples:**

```xml
<!-- Form scroll -->
<ScrollPane fx:id="formScroll" styleClass="module-form-scroll"
            VBox.vgrow="ALWAYS"
            hbarPolicy="NEVER" vbarPolicy="AS_NEEDED">
    <VBox styleClass="module-form-card" ...>...</VBox>
</ScrollPane>
```

```xml
<!-- Dashboard / Reports -->
<ScrollPane fitToWidth="true"
            hbarPolicy="NEVER" vbarPolicy="AS_NEEDED"
            styleClass="page-scroll" VBox.vgrow="ALWAYS"
            maxWidth="Infinity" maxHeight="Infinity">
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Controller access (`formScroll`) |
| `styleClass` | Scroll styling |
| `fitToWidth` | Content width matches viewport (dashboard/reports) |
| `hbarPolicy` | Horizontal bar: we use `NEVER` |
| `vbarPolicy` | Vertical bar: `AS_NEEDED` |
| `VBox.vgrow` | Take remaining vertical space |
| `maxWidth` / `maxHeight` / `minHeight` | Flex sizing |

---

### 1.11 FlowPane

**What it is:** Lays children out in a wrapping flow (like text wrap) — left to right, then next line.

**Where we use it:** Reports filter bar only.

**Example:**

```xml
<FlowPane styleClass="reports-filter-bar" maxWidth="Infinity">
    <VBox styleClass="reports-filter-field">
        <Label text="Project" .../>
        <ComboBox fx:id="projectCombo" .../>
    </VBox>
    <VBox styleClass="reports-filter-field">...</VBox>
    <HBox styleClass="reports-filter-actions">
        <Button text="Apply" .../>
    </HBox>
</FlowPane>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `styleClass` | Filter bar look |
| `maxWidth` | Use full page width |

---

## 2. Controls

### 2.1 Button

**What it is:** Clickable action control.

**Where we use it:** Nav, Logout, Login, Save/Cancel, Add, pagination, password toggle, Apply filter.

**Examples:**

```xml
<!-- Sidebar nav (text empty; graphic holds icon+label) -->
<Button text="" styleClass="nav-action" HBox.hgrow="ALWAYS"
        mnemonicParsing="false" focusTraversable="false"
        onAction="#onNavDashboard"
        alignment="CENTER_LEFT" maxWidth="Infinity">
    <graphic>
        <HBox styleClass="nav-content" mouseTransparent="true">...</HBox>
    </graphic>
</Button>
```

```xml
<!-- Form actions -->
<Button fx:id="saveButton" text="Save" styleClass="module-form-btn-save"
        mnemonicParsing="false" onAction="#onSave"/>
```

```xml
<!-- List page -->
<Button fx:id="addButton" text="+ Add Invoice" styleClass="module-add-btn"
        mnemonicParsing="false" onAction="#onAdd"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Controller field |
| `text` | Visible label (often `""` for nav) |
| `styleClass` | Button variant CSS |
| `onAction` | Click handler method |
| `mnemonicParsing` | Usually `false` |
| `focusTraversable` | Often `false` for nav/logout/toggle |
| `alignment` | Content alignment (`CENTER_LEFT` for nav) |
| `maxWidth` | Stretch |
| `HBox.hgrow` | Expand in nav row |
| `StackPane.alignment` | Password toggle position |

**Child property:** `<graphic>` — icon/label content for nav and eye toggle.

---

### 2.2 Label

**What it is:** Non-editable text (titles, field labels, values, required `*`).

**Where we use it:** Everywhere.

**Examples:**

```xml
<Label text="THALAM" styleClass="brand-title"/>
<Label fx:id="moduleTitleLabel" text="Materials" styleClass="module-list-title"/>
<Label text="*" styleClass="module-form-required"/>
<Label fx:id="grandTotalLabel" text="—" styleClass="module-form-grand-total-value"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Dynamic text from controller |
| `text` | Display string |
| `styleClass` | Typography / color |
| `alignment` | Text alignment (`CENTER_RIGHT` on some columns) |
| `mouseTransparent` | Sub-nav labels inside buttons |
| `prefWidth` / `minWidth` / `maxWidth` | Fixed/flex widths (dashboard overdue headers) |
| `HBox.hgrow` | Take leftover horizontal space |
| `GridPane.columnIndex` | Place in bulk header grid |

---

### 2.3 TextField

**What it is:** Single-line text input.

**Where we use it:** Login username; form fields; list search; currency amount inputs.

**Examples:**

```xml
<!-- Login -->
<TextField fx:id="usernameField" promptText="Enter your name"
           styleClass="custom-textfield" maxWidth="Infinity"
           onAction="#onLogin"/>

<!-- List search -->
<TextField fx:id="searchField" styleClass="module-search-field"
           promptText="Search materials..."/>

<!-- Form -->
<TextField fx:id="clientNameField" styleClass="module-form-input"
           promptText="Enter client name"/>

<!-- Currency row -->
<TextField fx:id="amountField" styleClass="module-form-currency-input"
           promptText="Enter amount" HBox.hgrow="ALWAYS" maxWidth="Infinity"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Read/write value in controller |
| `promptText` | Placeholder hint |
| `styleClass` | Input styling |
| `maxWidth` | Stretch |
| `onAction` | Enter key (login) |
| `HBox.hgrow` | Grow inside currency `HBox` |

---

### 2.4 PasswordField

**What it is:** Text field that masks characters.

**Where we use it:** Login only.

**Example:**

```xml
<PasswordField fx:id="passwordField" promptText="Enter your password"
               styleClass="password-field"
               maxWidth="Infinity" onAction="#onLogin"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Controller access |
| `promptText` | Placeholder |
| `styleClass` | Password styling |
| `maxWidth` | Stretch |
| `onAction` | Enter submits login |

---

### 2.5 TextArea

**What it is:** Multi-line text input.

**Where we use it:** Notes, address, work description on forms + Material popup.

**Examples:**

```xml
<TextArea fx:id="notesField"
          styleClass="text-area module-form-textarea module-form-notes"
          wrapText="true" promptText="Enter notes"/>

<TextArea fx:id="addressField"
          styleClass="text-area module-form-textarea module-form-notes"
          wrapText="true" promptText="Enter Address"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Controller access |
| `promptText` | Placeholder |
| `styleClass` | Textarea CSS (often multiple classes) |
| `wrapText` | Soft-wrap long lines (`true` everywhere we use it) |

---

### 2.6 ComboBox

**What it is:** Dropdown select list.

**Where we use it:** Forms (type, project, category, payment mode, unit…); list filters; reports filters; Material popup units.

**Examples:**

```xml
<!-- Empty combo — items filled in Java -->
<ComboBox fx:id="projectCombo" styleClass="module-form-combo"
          promptText="Select project" maxWidth="Infinity"/>

<!-- Items declared in FXML -->
<ComboBox fx:id="unitCombo" styleClass="module-form-combo"
          promptText="Select unit" maxWidth="Infinity">
    <items>
        <FXCollections fx:factory="observableArrayList">
            <String fx:value="Bag (Bag)"/>
            <String fx:value="Kilogram (Kg)"/>
        </FXCollections>
    </items>
</ComboBox>

<!-- Default selected value (Reports) -->
<ComboBox fx:id="dateRangeCombo" styleClass="reports-filter-combo" prefWidth="200">
    <items>...</items>
    <value>
        <String fx:value="This Month"/>
    </value>
</ComboBox>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Controller access |
| `promptText` | Shown when nothing selected |
| `styleClass` | Combo styling |
| `prefWidth` | Fixed width (filters) |
| `maxWidth` | Stretch in forms |

**Child properties:** `<items>`, `<value>` (reports defaults).

---

### 2.7 CheckBox

**What it is:** On/off toggle with a label.

**Where we use it:** Forms — Mark as Paid; Employee app access.

**Examples:**

```xml
<CheckBox fx:id="markAsPaidCheck" text="Mark as Paid"
          styleClass="module-form-checkbox"/>

<CheckBox fx:id="employeeAppAccessCheck"
          text="Allow this employee to access this app"
          styleClass="module-form-checkbox"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Read selected state |
| `text` | Label beside checkbox |
| `styleClass` | Checkbox CSS |

---

### 2.8 DatePicker

**What it is:** Calendar date chooser.

**Where we use it:** Add forms (expense, invoice, purchase, project, worklog).

**Examples:**

```xml
<DatePicker fx:id="datePicker" styleClass="module-form-datepicker"
            promptText="Select date" maxWidth="Infinity"/>

<DatePicker fx:id="invoiceDatePicker" styleClass="module-form-datepicker"
            promptText="Select invoice date" maxWidth="Infinity"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Get `LocalDate` in controller |
| `promptText` | Empty-state hint |
| `styleClass` | DatePicker CSS |
| `maxWidth` | Stretch in field block |

---

### 2.9 TableView

**What it is:** Spreadsheet-like data table.

**Where we use it:** All list pages (Clients, Materials, Expenses, Invoices, …).

**Examples:**

```xml
<TableView fx:id="recordsTable" styleClass="module-list-table"
           VBox.vgrow="ALWAYS">
    <columns>
        <TableColumn fx:id="serialColumn" text="No"
                     prefWidth="64.0" minWidth="64.0" maxWidth="64.0"
                     resizable="false" sortable="true"/>
        <TableColumn fx:id="name" text="Material Name"
                     minWidth="160.0" maxWidth="5000.0" sortable="true"/>
        ...
    </columns>
    <columnResizePolicy>
        <TableView fx:constant="CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS"/>
    </columnResizePolicy>
</TableView>
```

```xml
<!-- Some Scene Builder exports also set heights -->
<TableView fx:id="recordsTable"
           maxHeight="Infinity" minHeight="0.0" prefHeight="0.0"
           styleClass="module-list-table" VBox.vgrow="ALWAYS">
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Bind items / selection in controller |
| `styleClass` | Table look |
| `prefHeight` / `minHeight` / `maxHeight` | Flex height (often `0` + `ALWAYS` grow) |
| `VBox.vgrow` | Fill remaining list page height |

**Child properties:** `<columns>`, `<columnResizePolicy>`.

**Note:** The nested `<TableView fx:constant="CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS"/>` is how FXML references the static resize policy constant — columns share width instead of showing a blank trailing area.

---

### 2.10 TableColumn

**What it is:** One column definition inside a `TableView`.

**Where we use it:** List pages only.

**Examples:**

```xml
<!-- Fixed narrow column -->
<TableColumn fx:id="serialColumn" text="No"
             prefWidth="64.0" minWidth="64.0" maxWidth="64.0"
             resizable="false" sortable="true"/>

<!-- Flexible data column -->
<TableColumn fx:id="name" text="Vendor Name"
             minWidth="160.0" maxWidth="5000.0" sortable="true"/>

<!-- Action column (not sortable) -->
<TableColumn fx:id="actionsColumn" text="Action"
             prefWidth="88.0" minWidth="88.0" maxWidth="88.0"
             resizable="false" sortable="false"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Wire cell factories / values in controller |
| `id` | Occasional CSS/id (e.g. Vendors serial column) |
| `text` | Column header label |
| `prefWidth` | Preferred width |
| `minWidth` / `maxWidth` | Width clamps (`5000` ≈ “can grow a lot”) |
| `resizable` | User can drag column edge (`false` for No/ID/Action) |
| `sortable` | Click header to sort |

---

### 2.11 ListView

**What it is:** Scrollable list of items (selection list).

**Where we use it:** Add Project — associate employees only.

**Example:**

```xml
<ListView fx:id="employeesList" styleClass="module-form-list"
          prefHeight="140.0" maxWidth="Infinity"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:id` | Fill items / multi-select in controller |
| `styleClass` | List styling |
| `prefHeight` | Visible height |
| `maxWidth` | Stretch full form width |

---

### 2.12 Hyperlink

**What it is:** Clickable text that looks like a link.

**Where we use it:** Login (Forgot password / Sign up); Dashboard “view All” links.

**Examples:**

```xml
<Hyperlink text="Forgot password?" styleClass="forgot-password-link"
           focusTraversable="false"/>

<Hyperlink text="view All" styleClass="view-all-link"
           onAction="#onViewAllMaterials"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `text` | Link label |
| `styleClass` | Link look |
| `onAction` | Navigation / action |
| `focusTraversable` | Often `false` so Tab skips it |

> Note: Many FXML files still `<?import Hyperlink?>` because of shared sidebar history, but the live Hyperlink nodes are mainly Login + Dashboard.

---

## 3. Media & helper types

### 3.1 ImageView

**What it is:** Displays an image at a given size.

**Where we use it:** Brand logo, nav icons, login avatar, password eye icon.

**Examples:**

```xml
<ImageView fitWidth="40.0" fitHeight="40.0" preserveRatio="true"
           styleClass="brand-logo">
    <image>
        <Image url="@../Assets/Images/thalam-logo.png"/>
    </image>
</ImageView>

<ImageView fitWidth="20.0" fitHeight="20.0" preserveRatio="true"
           smooth="true" styleClass="nav-icon">
    <image>
        <Image url="@../Assets/Images/dashboard.png"
               requestedWidth="64" requestedHeight="64" preserveRatio="true"/>
    </image>
</ImageView>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fitWidth` / `fitHeight` | Display size on screen |
| `preserveRatio` | Keep aspect ratio |
| `smooth` | Better scaling quality |
| `pickOnBounds` | Hit-testing uses bounds (eye icon) |
| `styleClass` | CSS hooks |

**Child property:** `<image>`.

---

### 3.2 Image

**What it is:** The image data/source loaded into an `ImageView`.

**Examples:**

```xml
<Image url="@../Assets/Images/thalam-logo.png"/>

<Image url="@../Assets/Images/clients.png"
       requestedWidth="64" requestedHeight="64" preserveRatio="true"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `url` | Path relative to FXML (`@../Assets/...`) |
| `requestedWidth` / `requestedHeight` | Decode/load size hint (nav icons) |
| `preserveRatio` | Keep ratio while loading at requested size |

---

### 3.3 Insets

**What it is:** Padding values (top, right, bottom, left).

**Where we use it:** Login outer `VBox` padding only.

**Example:**

```xml
<padding>
    <Insets top="48" right="64" bottom="40" left="64"/>
</padding>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `top` / `right` / `bottom` / `left` | Padding in pixels |

---

### 3.4 FXCollections

**What it is:** Factory helper to build an `ObservableList` in FXML.

**Where we use it:** Material unit ComboBox; Reports filter ComboBoxes.

**Example:**

```xml
<FXCollections fx:factory="observableArrayList">
    <String fx:value="Bag (Bag)"/>
    <String fx:value="Kilogram (Kg)"/>
</FXCollections>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:factory` | Always `observableArrayList` in this project |

---

### 3.5 String

**What it is:** A string value node for ComboBox items / default values.

**Example:**

```xml
<String fx:value="This Month"/>
```

**Attributes used**

| Attribute | Purpose |
|-----------|---------|
| `fx:value` | The actual string content |

---

## 4. Child property elements

These are **not** layout nodes themselves — they set a named property on the parent:

| Element | Parent | Purpose in Thalam |
|---------|--------|-------------------|
| `<graphic>` | `Button` | Icon + label content (nav, eye toggle) |
| `<image>` | `ImageView` | Holds `Image` |
| `<padding>` | `VBox` | Holds `Insets` (login) |
| `<columnConstraints>` | `GridPane` | Holds `ColumnConstraints` list |
| `<rowConstraints>` | `GridPane` | Holds `RowConstraints` (dashboard) |
| `<items>` | `ComboBox` | Holds `FXCollections` |
| `<value>` | `ComboBox` | Default selected item (reports) |
| `<columns>` | `TableView` | Holds `TableColumn`s |
| `<columnResizePolicy>` | `TableView` | Constrained resize policy |
| `<left>` / `<right>` | `BorderPane` | Footer Cancel / Save |

---

## 5. Constraint attributes cheat sheet

| Attribute | Means | Typical values in Thalam |
|-----------|--------|---------------------------|
| `AnchorPane.topAnchor` | Distance from top | `0.0` |
| `AnchorPane.rightAnchor` | Distance from right | `0.0` |
| `AnchorPane.bottomAnchor` | Distance from bottom | `0.0` |
| `AnchorPane.leftAnchor` | Distance from left | `0.0` |
| `HBox.hgrow` | Grow horizontally in HBox | `ALWAYS` |
| `VBox.vgrow` | Grow vertically in VBox | `ALWAYS` |
| `StackPane.alignment` | Position in StackPane | `TOP_LEFT`, `CENTER_RIGHT` |
| `GridPane.columnIndex` | Column number (0-based) | `0`, `1`, `2` |
| `GridPane.rowIndex` | Row number (0-based) | `0`, `1`, … |
| `GridPane.columnSpan` | Span multiple columns | `2` (full-width fields) |
| `GridPane.hgrow` | Column grow for that child | `ALWAYS` |
| `GridPane.vgrow` | Row grow for that child | `NEVER` / `ALWAYS` |
| `GridPane.valignment` | Vertical align in cell | `TOP` |

**`visible` vs `managed`:**  
- `visible="false"` → hidden but may still reserve space.  
- `managed="false"` → layout skips it (no gap).  
We usually set **both** for conditional sections (`paymentModeSection`, `projectSection`, closed `nav-kids`).

---

## 6. Quick index

| Component | Main role | Typical screens |
|-----------|-----------|-----------------|
| `AnchorPane` | Screen root | All except Login & Material popup |
| `StackPane` | Layers / sidebar | Login, sidebar, password wrap |
| `VBox` | Vertical layout | Everywhere |
| `HBox` | Horizontal layout | Shell, nav, headers |
| `BorderPane` | Cancel \| Save footer | Forms, popup |
| `GridPane` | Form / dash grid | Forms, dashboard |
| `ColumnConstraints` | Column widths | Grids |
| `RowConstraints` | Row grow | Dashboard |
| `Region` | Spacer / CSS icon | Login, nav, dash, reports |
| `ScrollPane` | Scroll content | Forms, dash, reports |
| `FlowPane` | Wrapping filters | Reports |
| `Button` | Actions | Everywhere |
| `Label` | Text | Everywhere |
| `TextField` | Single-line input | Login, forms, search |
| `PasswordField` | Masked input | Login |
| `TextArea` | Multi-line input | Forms, popup |
| `ComboBox` | Dropdown | Forms, filters, reports |
| `CheckBox` | Boolean flag | Paid / app access |
| `DatePicker` | Date | Forms |
| `TableView` | Data table | List pages |
| `TableColumn` | Table column | List pages |
| `ListView` | Selectable list | Add Project |
| `Hyperlink` | Text link | Login, Dashboard |
| `ImageView` / `Image` | Pictures | Brand, nav, login |
| `Insets` | Padding | Login |
| `FXCollections` / `String` | Combo items | Materials, Reports |

---

## Related notes (not FXML nodes)

These appear in **Java**, not as FXML tags, but pair with the same UI:

| Java API | Used for |
|----------|----------|
| `TextInputDialog` | “+ Add Category” prompt (Expenses / Worklog) |
| `Stage` + `FXMLLoader` | Material popup window |
| `Alert` | Validation / confirm messages (where used) |

---

*Generated from the Thalam codebase FXML inventory. When you add a new screen, reuse the matching template (list / form / popup) and prefer the same attributes already listed above.*
