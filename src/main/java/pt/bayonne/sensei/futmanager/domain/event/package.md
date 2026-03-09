# Domain Event Package

Domain event package containing domain events.

This package contains domain events that represent significant occurrences within the domain.
Domain events are used to communicate between different parts of the application and
to trigger side effects when something important happens in the domain.

Domain events:
- Are **immutable value objects**
- Represent **something that happened in the past**
- Are **named with past-tense verbs** (e.g., PlayerTransferred, MatchCompleted)
- Contain **all the information related to the event**

Examples of domain events in a football manager application might include:
- **PlayerTransferredEvent**: Triggered when a player is transferred between teams
- **MatchCompletedEvent**: Triggered when a match is completed
- **TeamPromotedEvent**: Triggered when a team is promoted to a higher division