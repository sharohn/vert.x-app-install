# App Installations System

A system in Vert.x that automates the installation and updating of apps on Miko Mini,
similar to Playstore/Appstore, without any user interface. The system will handle app
installations sequentially, track the progress through various states, and automatically retry
failed installations. The system will also provide detailed analytics for each installation.

## Tech Stack

  * Vert.x
  * Postgres
