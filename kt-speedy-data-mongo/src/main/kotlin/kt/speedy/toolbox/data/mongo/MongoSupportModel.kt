package kt.speedy.toolbox.data.mongo

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.*
import java.io.Serializable
import java.util.*

open class MongoSupportModel : Serializable {
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    lateinit var id: ObjectId

    @CreatedDate
    var createdDate: Date? = null

    @LastModifiedDate
    var lastModifiedDate: Date? = null

    @CreatedBy
    var createdBy: String? = null

    @LastModifiedBy
    var lastModifiedBy: String? = null

    /**
     * 删除标记
     */
    var deleted: Boolean = false

}