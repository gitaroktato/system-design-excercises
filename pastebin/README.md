# Pastebin

## Functional Requirements
* Users can upload text and get a unique URL
* Pastes should expire over time. Expiration can be customized
* A paste can be additionally protected with a specified password
* A custom URL alias can be picked for the paste if it's not taken

## Non-functional requirements

### Security
- Paste URLs should not be guessed by unauthorized users by a simple enumeration. 


### Auditability
- We should collect popularity of each paste for further analysis and collecting usage statistics.
- A specific user is a power-user if that person's average paste reaches 1,000,000 views. 
