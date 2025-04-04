package kt.speedy.toolbox.data.jpa

import jakarta.persistence.*
import kt.speedy.toolbox.data.jpa.util.IdKit
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class SupportModal : Serializable {
    @Id
    @Comment("主键")
    open var id: String? = null

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    @Comment("创建时间")
    open var createdDate: Date? = null

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date")
    @Comment("修改时间")
    open var lastModifiedDate: Date? = null

    @Column(name = "created_by")
    @CreatedBy
    @Comment("创建人")
    open var createdBy: String? = null

    @Column(name = "last_modified_by")
    @LastModifiedBy
    @Comment("修改人")
    open var lastModifiedBy: String? = null

    /**
     *
     */
    @Comment("删除标识")
    open var deleted: Boolean? = false

    init {
        if (id == null) id = IdKit.get()
    }
}