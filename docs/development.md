# Activities flow

[State machine](activities_flow.mmd)

# Persistence management

There are three levels of state persistence.

1. **View Model level.** There is ViewModel class that allows data to survive configuration changes such as screen rotations. Each stateful activity has a view model class (a class
   extending the `ViewModel`).

2. **Saved instance state.** Data saved this way survives system initiated process death (e.g. removing the process due to lack of memory). Saving instance state should be done
   through view model. The view model class aggregates an instance of the `SavedStateHandle`. When the data is changed, the view model should
   call `SavedStateHandle.set(variable, value)` (
   the map syntax can be used too).  
   When initializing the view model, available data should be loaded from `SavedStateHandle.get()`.

3. **Persistent storage.** To save data that should remain available till the user decides to remove it. It should by done through serialization to XML and then saving to disc
   using the `StorageHelper` class. The data that supposed to be saved should be wrapped by the view model, and the view model should handle its lifecycle. The view model should be
   aware of changes in such data and persist it immediately after the change.

# Permissions

## Structure

[Class diagram](permissions_class_diagram.mmd)

## Requesting permissions

[Sequence diagram](requesting_permissions_sequence_diagram.mmd)

## Handling request permission result

[Sequence diagram](handling_request_result.mmd)

Rejecting required permissions should result in a dialog where user can change his/her mind. Permissions can be rejected "permanently", then the settings need to be visited to
grant permissions.

# Releasing

You need to push a tag to create a release. Execute:

```
git tag
```

to check existing tags (only the local ones will be printed).

To create a new release execute:

```
git tag -a X.Y -m "comment"
git push origin --tags
```

It will launch a pipeline instance that creates a new release.
