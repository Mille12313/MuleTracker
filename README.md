# MuleTracker 1.3
MuleTracker is a comprehensive plugin designed to assist server administrators in tracking and recovering stolen items seamlessly. By integrating with CoreProtect and offering configurable item tracking features, MuleTracker streamlines the process of locating specific items, providing an effective solution for managing item theft on the server.


Users can find a CSV file in the 'logs' directory containing comprehensive information on item tracking and logging. The CSV file includes the following details:
## features
- **Logs Configured Items:** Tracks all the items that have been configured.
- **Logs Location** 
- **Logs Action:** 
- **Logs players**
- **Logs Date & Time**
- **Logs world of where the action takes place.**

Provides information on the location, player, world, and timestamp associated with each tracked item event.
Describes the specific action or event related to the tracked item (e.g., pickup, drop, transfer).

Users have the flexibility to customize the items they want to trace based on their preferences. The plugin allows for in-game reloading with the command `/muletracker reload`, enabling users to apply configuration changes without restarting the server.

This feature not only enhances the user experience but also offers a dynamic and adaptable solution for tracking and recovering items on the server.

## Commands
- /muletracker logs (alias MT) - Gives you the latest 100 logs or 30 days.
- /muletracker logs <playername> - Lookup specific logs from this player.
- /muletracker reload - Reloads the config.yml

## Permissions
- muletracker.reload
- muletracker.logs
