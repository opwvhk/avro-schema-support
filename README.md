IntelliJ plugin supporting Apache Avro™ IDL
===========================================

Plugin for IntelliJ to handle Avro™ IDL files.

The plugin currently supports:
* Full parsing of Avro `.avdl` files, including new logical types like `local_timestamp_ms`
* Syntax highlighting, formatting and settings
* Code completion based on syntax and supported references
* Checks for some semantic errors, like unknown symbols, invalid identifiers, and values for special annotations like `@namespcae`, `@aliases`, `@order` and `@logicalType`
* Inspection (& quick fix) for duplicate annotations
* Rename support for named schemas (i.e. renaming them also updated their references)
* Miscellaneous stuff like the structure view, "find usages" and "go to symbol"

Planned changes (if/when my job allows me to, so feel free to help):
* Support for imported schemas
* Inspections on naming conventions (identifying Python and Java naming conventions)
