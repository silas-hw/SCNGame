Welcome to SCNGame! This page is dedicated to roughing out what should be done and how we should go about doing it. It's effectively a small scratch pad for me to figure things out in terms of project management and is consequently incredibly informal.

## Development Stages

The development of SCNGame will follow two discrete stages; However, the majority of development will reside in the latter of two stages - the first is just to get things up and running.
### Pre-Dev
This stage prioritises research and prototyping to understand *what* game we should make and *how* we should make it.

It's worth noting that many of the things done here will still be done beyond pre-dev, but this stage focuses all its time on these tasks. Such tasks include:
- Determining a rough project plan and goals
- Determining what tools and techniques need to be researched and learnt to facilitate development
- Learning (at least) the basics of these tools
- Producing low-fidelity prototypes to get an idea of how things work
This should only consume about a months worth of development
### In-Dev
This is where the meat begins! We actually write some fricking code!!

This is the stage where 'normal' development takes place - we follow through three sub-phases building up to a significant release of the game:
- **alpha**: coding to implement a requirement or set of requirements with internal testing (automated and manual)
- **beta**: coding to refine an implemented requirement with external testing (play testers)
- **release candidate**: refining prior to release to ensure quality and integrity
Development will continuously cycle between these three phases.

## Development Flow
We will follow an agile approach to development following continuous integration. This means having one main-line branch to make the majority of commits to, then branching out for release.

We will loosely follow scrum development - but with much less rigidity and accounting for a much lower velocity and throughput (Casper and Nathan both have jobs outside of making this game, and a single dev can only do so much!). 
### Sprints
#### Pre-Dev Sprints
Pre-dev won't consist of sprints. It's just gonna be some messy prototyping and sketching to get our footing. 
#### In-Dev Sprints
An in-dev sprint will last 2 weeks.

Each sprint will start with a meeting to determine any new product requirements, as well as analysing the pre-existing backlog. We will then allocate tasks based on the backlog and create git issues for completing these tasks.

Halfway between each sprint, we will hold an 'in-review' meeting where we discuss our current progress and if throughput needs to be adjusted or tasks altered

At the end of each sprint, I will form a sprint retrospective analysing the work of the sprint and how future work may need to change based on it.

## Task Assignment
Tasks will be displayed on a Kanban board on a GitHub Project. There will be no prioritisation - you simply pick up a task you feel like doing. The only consideration that needs to be taken is whether or not a task is 'ready' or not (i.e. does it have any other dependencies that need to be completed first). Unready tasks will be placed in the 'backlog'. However, there will be a limit on the number of items in the backlog. This is because we may discover things we want to do later to be different after completing that things dependency.

There will also be a limit on the number of tasks that can be present in 'ready', 'in progress' and 'in review' to prevent an overbearing quantity of work being considered at once. We develop incrementally! A quantity of small changes leads to a whole project. Rome wasn't built in a day.

### Dependent Tasks
Say we want to create some form of shop system. This is dependent on a dialog system being in place, which is in turn dependent on the UI system being in place. If we create all the tasks for the shop system and put them in the backlog before even implementing the UI, we may discover (as a result of implementing the UI) that the way we want the shop system to work to be completely different to how we initially planned. Having a limit on the number of tasks in the backlog encourages only creating dependent tasks when they are close to being ready and mitigates the risk of this happening.
