---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

Target User: Alex
* Alex is a young, fresh financial advisor (FA).
* He recently graduated and is eager to build a strong client base.
* He wants to grow his career by maintaining meaningful client relationships.
* He struggles with tracking numerous client interactions and follow-ups.
* He often forgets key touchpoints like birthdays and policy renewals.
* He needs a system that reminds him who to contact and when to engage with clients.
* Sometimes, he feels overwhelmed managing client relationships manually.
* He can type fast and prefers a CLI interface over mouse inputs


**Value proposition**: Young financial advisors often struggle with managing a high volume of client interactions, making it easy to miss key follow-ups and important milestones. Keeping track of numerous touchpoints manually can feel overwhelming, leading to missed opportunities and weaker client relationships. ClientNest helps young financial advisors stay organized, manage clients more effectively, build stronger relationships, enhance trust, and focus on growing their business.



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                         | I want to …​                                                                                        | So that I can…​                                                                   |
|----------|---------------------------------|-----------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `* `     | young financial advisor         | be able to add clients and client details                                                           | track the clients                                                                 |
| `* `     | young financial advisor         | view all my client interactions in one application                                                  | not miss any important follow-up opportunities                                    |
| `* `     | young financial advisor         | be able to delete any non-current client information                                                | have an overview of only the relevant clients                                     |
| `* `     | young financial advisor         | access usage instructions                                                                           | easily refer to them when I need help using the app                               |
| `* *`    | young financial advisor         | be guided through the initial setup process                                                         | start using the system quickly and efficiently                                    |
| `* * `   | young financial advisor         | access a help center or FAQ section                                                                 | quickly find answers to common questions                                          |
| `* * `   | young financial advisor         | set and receive reminders for important client touchpoints                                          | maintain meaningful relationships with my clients                                 |
| `* * *`  | young financial advisor         | sync ClientNest with my calendar                                                                    | manage my time and appointments more efficiently                                  |
| `* * *`  | young financial advisor         | have the system analyze client engagement history and suggest optimal times for follow-ups          | proactively nurture relationships and never miss a chance to connect              |
| `* * *`  | young financial advisor         | have a checklist for each client meeting                                                            | ensure all necessary points are covered and nothing is missed                     |
| `* * *`  | young financial advisor         | categorize client interactions by type (e.g., phone call, email, in-person meeting)                 | see the variety of communication methods used and stay on top of all touchpoints  |
| `* `     | young financial advisor         | add a detailed description to client profiles                                                       | remember important personal details about each client                             |
| `* *`    | young financial advisor         | store links to my clients' policies                                                                 | easily access important documents and provide timely support when needed          |
| `* *`    | intermediate financial advisor  | receive notifications for missed follow-ups                                                         | avoid letting important interactions slip through the cracks                      |
| `* *`    | intermediate financial advisor  | track the status of each client (e.g., potential, active, dormant)                                  | prioritize my efforts and focus on high-value opportunities                       |
| `* * *`  | intermediate financial advisor  | create a visual timeline of client interactions and goals                                           | easily track progress and plan future actions                                     |
| `* *`    | intermediate financial advisor  | receive notifications when a client's policy is up for renewal                                      | proactively reach out to clients for reviews or updates                           |
| `* * *`  | intermediate financial advisor  | create client engagement plans (e.g., weekly check-ins, monthly newsletters)                        | structure my interactions effectively                                             |
| `* * *`  | intermediate financial advisor  | assign tasks related to clients (e.g., send documents, schedule meetings)                           | stay organized and accountable                                                    |
| `* *`    | intermediate financial advisor  | search my client base by name, date of last contact, or milestone                                   | quickly find relevant information and stay on top of important interactions       |
| `* *`    | intermediate financial advisor  | store client preferences (e.g., communication method, preferred times)                              | tailor my outreach to each client’s needs                                         |
| `* *`    | intermediate financial advisor  | keep track of client milestones (e.g., policy anniversaries, life events)                           | celebrate and engage with them at meaningful times                                |
| `* *`    | intermediate financial advisor  | have a history of client interactions available in the system                                       | review past conversations and provide better service                              |
| `* * *`  | intermediate financial advisor  | easily track client satisfaction and feedback                                                       | improve my service and relationships with clients                                 |
| `* * *`  | intermediate financial advisor  | have the system automatically log my client interactions                                            | avoid manually entering every detail and focus on building strong relationships   |
| `* *`    | expert financial advisor        | add personal notes about clients (e.g., preferences, key conversations)                             | engage with them on a more personal level and deepen our relationship             |
| `* * *`  | expert financial advisor        | automate the process of sending follow-up emails                                                    | avoid missing opportunities for ongoing engagement                                |
| `* * *`  | expert financial advisor        | have the system suggest best practices for client relationship management based on the data I input | improve my approach over time and build stronger, long-lasting relationships      |
| `* * *`  | expert financial advisor        | track my client retention rate                                                                      | assess my success in building long-term relationships                             |
| `* * *`  | expert financial advisor        | track all my client interactions by topic (e.g., retirement, investment, insurance)                 | track ongoing discussions more effectively and address client concerns accurately |

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: View Client List Overview**

**MSS**

1. User requests to list all clients.
2. AddressBook retrieves and displays a list of clients.

   Use case ends.

**Extensions**

* 2a. Client retrieval issues.
    * 2a1. AddressBook shows an error message.

      Use case ends.

* 2b. Deleted clients continue to show up.
    * 2b1. AddressBook refreshes the client list and removes deleted clients.

      Use case resumes at step 2.

---

**Use case: Add a Client**

**MSS**

1. User requests to add a new client with details (name, optional fields).
2. AddressBook validates the input fields.
3. AddressBook adds the client to the database.
4. AddressBook confirms the addition.

   Use case ends.

**Extensions**

* 2a. One or more input fields are invalid.
    * 2a1. AddressBook shows an error message:
        * "Please input valid name" (if name is missing or invalid).
        * "Please input valid address" (if address is invalid).
        * "Please input valid phone number" (if phone number is invalid).
        * "Please input valid email" (if email is invalid).
        * "Please input valid birthday" (if birthday format is incorrect).
        * "Please input valid premium" (if premium field is invalid).
    * 2a2. Use case resumes at step 1.

* 3a. Client already exists in the database.
    * 3a1. AddressBook shows an error message and displays the existing client details.

      Use case ends.

* 3b. Database error occurs while saving the client.
    * 3b1. AddressBook shows an error message.

      Use case ends.

---

**Use case: Delete a Client**

**MSS**

1. User requests to delete a specific client by name.
2. AddressBook validates the input.
3. AddressBook deletes the client from the database.
4. AddressBook confirms the deletion.

   Use case ends.

**Extensions**

* 2a. The provided name is invalid or empty.
    * 2a1. AddressBook shows an error message: "Please input valid name."

      Use case resumes at step 1.

* 3a. The client does not exist in the database.
    * 3a1. AddressBook shows an error message: "Client not found."

      Use case ends.

* 3b. A database error occurs while deleting the client.
    * 3b1. AddressBook shows an error message.

      Use case ends.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. The system should be able to handle up to 1000 clients and their records without noticeable degradation in response time during typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The system should ensure that client data is accurate and consistent throughout its lifecycle. No data should be lost, corrupted, or incorrectly altered during normal operations or when interacting with the system.
5. The application should provide a clean, intuitive, and user-friendly interface. It should be easy for financial advisors with varying levels of technical expertise to navigate, with minimal learning required.
6. The system's search functionality should return relevant client and interaction data in less than 1 second, even when the database contains a significant amount of records (up to 1000 clients).
7. The system should process and respond to common actions, such as adding, updating, or viewing clients, within 2 seconds, ensuring smooth and efficient user interactions.
8. The system should be able to export client data in common, standard formats (e.g., CSV, Excel, JSON) to allow for easy transfer or analysis of data.
9. The application should ensure the confidentiality and integrity of client data, employing encryption for sensitive data both in transit and at rest
10. The system should be able to recover from unexpected failures, such as crashes or server downtime, and resume normal operation without data loss or corruption. The recovery process should complete within a reasonable time (e.g., within 5 minutes).
11. The system should remain functional and should not crash under conditions like incorrect user inputs, unexpected data formats, or server overload. It should handle errors gracefully and provide users with meaningful error messages without interrupting the overall user experience.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Client**: An individual or organization for whom financial advisory services are provided, whose details are stored within the system.
* **Sensitive Data**: Information that must be protected due to privacy or confidentiality requirements, including personal, financial, or identification details
* **Interaction**: A record of any communication or engagement with a client, including meetings, calls, emails, and follow-ups

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
