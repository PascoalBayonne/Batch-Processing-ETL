# Domain Service Package

Domain service package containing domain services.

This package contains domain services that encapsulate business logic that doesn't
naturally fit within an entity or value object. Domain services operate on multiple
domain objects and represent domain concepts that are processes or transformations
rather than things.

Domain services:
- Are **stateless operations**
- Represent **domain concepts that are processes or transformations**
- Operate on **multiple domain objects**
- Encapsulate **business logic that doesn't naturally fit within an entity or value object**

Examples of domain services in a football manager application might include:
- **MatchSimulationService**: Simulates a match between two teams
- **PlayerTransferService**: Handles the business logic of transferring a player between teams
- **TeamRankingService**: Calculates team rankings based on match results