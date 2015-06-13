package example.slick

import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext
import slick.lifted.TableQuery
import scala.concurrent.Future
import slick.driver.H2Driver.api._

object Schema {

  /**
   * http://stackoverflow.com/questions/15408489/how-to-execute-ddl-only-when-tables-dont-exist
   */
  def createTablesIfNotExistent(db: Database, tables: TableQuery[_ <: Table[_]]*): Future[Seq[Unit]] = {
    implicit val ec: ExecutionContext = db.executor.executionContext
    
    Future.sequence(
      tables map { table =>
        db.run(MTable.getTables(table.baseTableRow.tableName)).flatMap { result =>
          if (result.isEmpty) {
            db.run(table.schema.create)
          } else {
            Future.successful(())
          }
        }
      })
  }
}