package kt.speedy.toolbox.data.mongo

import com.mongodb.BasicDBList
import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.regex.Pattern
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


/**
 * 更新的值存在，则采用 set 操作，不存在，则采用 unset 操作
 */
fun Update.setOrUnset(key: String, value: Any?): Update {
    return if (value == null) {
        this.unset(key)
    } else {
        this.set(key, value)
    }
}

/**
 * 更新并返回数据
 */
fun <T> MongoTemplate.findAndModify(
    queryCriteria: Criteria,
    update: Update,
    ignoreFields: List<String>,
    entityClass: Class<T>
): T? {
    val query = Query.query(queryCriteria)
    val field = query.fields()
    
    ignoreFields.forEach {
        field.exclude(it)
    }
    
    return this.findAndModify(
        query,
        update,
        FindAndModifyOptions().returnNew(true),
        entityClass
    )
}

/**
 * 更新并返回数据
 */
fun <T> MongoTemplate.findAndModifyWithIncludeFields(
    queryCriteria: Criteria,
    update: Update,
    includeFields: List<String>,
    entityClass: Class<T>
): T? {
    val query = Query.query(queryCriteria)
    val field = query.fields()
    
    includeFields.forEach {
        field.include(it)
    }
    
    return this.findAndModify(
        query,
        update,
        FindAndModifyOptions().returnNew(true),
        entityClass
    )
}

/**
 * 根据主键进行数据更新
 */
fun <T> MongoTemplate.findAndModifyById(
    id: ObjectId,
    update: Update,
    ignoreFields: List<String>,
    entityClass: Class<T>
): T? {
    return this.findAndModify(
        Criteria("id").`is`(id),
        update,
        ignoreFields,
        entityClass
    )
}

/**
 * 根据主键进行数据更新
 */
fun <T> MongoTemplate.findAndModifyWithIncludeFieldsById(
    id: ObjectId,
    update: Update,
    includeFields: List<String>,
    entityClass: Class<T>
): T? {
    return this.findAndModifyWithIncludeFields(
        Criteria("id").`is`(id),
        update,
        includeFields,
        entityClass
    )
}

/**
 * 根据主键更新数据
 */
fun <T> MongoTemplate.updateById(id: ObjectId, update: Update, entityClass: Class<T>) {
    this.updateFirst(
        Query.query(Criteria.where("id").`is`(id)),
        update,
        entityClass
    )
}

/**
 * 根据主键更新数据
 */
fun <T> MongoTemplate.updateById(id: ObjectId, update: Update, collectionName: String) {
    this.updateFirst(
        Query.query(Criteria.where("id").`is`(id)),
        update,
        collectionName
    )
}


/**
 * 根据主键删除
 */
fun <T> MongoTemplate.removeById(id: ObjectId, entityClass: Class<T>) {
    deleteById(id, entityClass)
}

/**
 * 通过id删除
 */
fun <T> MongoTemplate.deleteById(id: ObjectId, entityClass: Class<T>) {
    this.remove(
        Query.query(Criteria.where("id").`is`(id)),
        entityClass
    )
}

/**
 * 通过id删除
 */
fun MongoTemplate.deleteById(id: ObjectId, collectionName: String) {
    this.remove(
        Query.query(Criteria.where("_id").`is`(id)),
        collectionName
    )
}

/**
 * 根据主键更新数据
 */
fun <T> MongoTemplate.updateMultiById(id: ObjectId, update: Update, entityClass: Class<T>): UpdateResult {
    return this.updateMulti(
        Query.query(Criteria.where("id").`is`(id)),
        update,
        entityClass
    )
}

/**
 * 根据id进行查询
 */
fun <T> MongoTemplate.findByIdWithIncludeFields(id: ObjectId, includeFields: List<String>, entityClass: Class<T>): T? {
    val query = Query.query(Criteria.where("id").`is`(id))
    val field = query.fields()
    
    includeFields.forEach {
        field.include(it)
    }
    return this.findOne(query, entityClass)
}

fun <T> MongoTemplate.findWithIncludeFields(
    query: Query,
    includeFields: List<String>,
    entityClass: Class<T>,
    collectionName: String? = null
): List<T> {
    val field = query.fields()
    includeFields.forEach {
        field.include(it)
    }
    return if (collectionName == null) this.find(query, entityClass) else this.find(query, entityClass, collectionName)
}

fun <T> MongoTemplate.findReturnPage(query: Query, page: Pageable, entityClass: Class<T>): Page<T> {
    val count = this.count(query, entityClass)
    query.with(page)
    val content = this.find(query, entityClass)
    return PageImpl<T>(content, page, count)
}

fun <T> MongoTemplate.findWithIncludeFieldsReturnPage(
    query: Query,
    includeFields: List<String>,
    page: Pageable,
    entityClass: Class<T>
): Page<T> {
    val field = query.fields()
    includeFields.forEach {
        field.include(it)
    }
    val count = this.count(query, entityClass)
    query.with(page)
    val content = this.find(query, entityClass)
    return PageImpl<T>(content, page, count)
}

/**
 * 根据id进行查询
 */
fun <T> MongoTemplate.findByIdWithExcludeFields(id: ObjectId, excludeFields: List<String>, entityClass: Class<T>): T? {
    val query = Query.query(Criteria.where("id").`is`(id))
    val field = query.fields()
    
    excludeFields.forEach {
        field.exclude(it)
    }
    return this.findOne(query, entityClass)
}

fun <T> MongoTemplate.findByIds(ids: List<ObjectId>, entityClass: Class<T>): List<T> {
    return this.find(Query.query(Criteria.where("id").`in`(ids)), entityClass)
}

/**
 * 根据id进行查询
 */
fun <T> MongoTemplate.findWithExcludeFieldsReturnPage(
    query: Query,
    excludeFields: List<String>,
    page: Pageable,
    entityClass: Class<T>
): PageImpl<T> {
    val field = query.fields()
    query.with(page)
    excludeFields.forEach {
        field.exclude(it)
    }
    val count = this.count(query, entityClass)
    val content = this.find(query, entityClass)
    return PageImpl<T>(content, page, count)
}


fun <T> MongoTemplate.findWithExcludeFields(
    criteria: Criteria,
    excludeFields: List<String>,
    entityClass: Class<T>
): List<T> {
    val query = Query.query(criteria)
    val field = query.fields()
    
    excludeFields.forEach {
        field.exclude(it)
    }
    
    return this.find(query, entityClass)
}

fun <T> MongoTemplate.findWithExcludeFields(query: Query, excludeFields: List<String>, entityClass: Class<T>): List<T> {
    val field = query.fields()
    
    excludeFields.forEach {
        field.exclude(it)
    }
    
    return this.find(query, entityClass)
}

fun <T> MongoTemplate.findWithIncludeFields(
    criteria: Criteria,
    includeFields: List<String>,
    entityClass: Class<T>
): List<T> {
    val query = Query.query(criteria)
    val field = query.fields()
    
    includeFields.forEach {
        field.include(it)
    }
    
    return this.find(query, entityClass)
}


fun <T> MongoTemplate.findOneWithIncludeFields(
    criteria: Criteria,
    includeFields: List<String>,
    entityClass: Class<T>
): T? {
    val query = Query.query(criteria)
    val field = query.fields()
    
    includeFields.forEach {
        field.include(it)
    }
    
    return this.findOne(query, entityClass)
}

fun <T> MongoTemplate.findOneWithExcludeFields(
    criteria: Criteria,
    includeFields: List<String>,
    entityClass: Class<T>
): T? {
    val query = Query.query(criteria)
    val field = query.fields()
    
    includeFields.forEach {
        field.exclude(it)
    }
    
    return this.findOne(query, entityClass)
}


fun String.toObjectId(): ObjectId {
    return ObjectId(this)
}

/**
 * orOperator
 */
fun Criteria.oor(vararg criteria: Criteria): Criteria {
    val dbo = this.criteriaObject["\$or"]
    if (dbo != null) {
        val bsonList = dbo as BasicDBList
        criteria.mapTo(bsonList) {
            it.criteriaObject
        }
    } else {
        this.orOperator(*criteria)
    }
    return this
}

/**
 * andOperator
 */
fun Criteria.aand(vararg criteria: Criteria): Criteria {
    val dbo = this.criteriaObject["\$and"]
    if (dbo != null) {
        val bsonList = dbo as BasicDBList
        criteria.mapTo(bsonList) {
            it.criteriaObject
        }
    } else {
        this.andOperator(*criteria)
    }
    return this
}

fun Criteria.withRegexCriteria(field: String, value: String?) : Criteria {
    return value?.getRegexCriteria(field)?:this
}

/**
 * withDeleted
 */
fun Criteria.withDeleted(deleted: Boolean?): Criteria {
    if (deleted != null) {
        this.and("deleted").`is`(deleted)
    }
    return this
}

fun Criteria.withHolderId(holderId: String): Criteria {
    this.and("createdBy.holderId").`is`(holderId)
    return this
}

fun Query.includeField(vararg field: String): Query {
    field.forEach {
        this.fields().include(it)
    }
    return this
}

fun Query.includeFields(fields: Array<String>): Query {
    fields.forEach {
        this.fields().include(it)
    }
    return this
}

fun Query.excludeFields(vararg field: String): Query {
    field.forEach {
        this.fields().exclude(it)
    }
    return this
}

/**
 * 获取一条数据的一个属性，只返回属性的值，提高查询效率
 * @param id 数据主键
 * @param field 要查询的字段名称
 */
fun <R, T> MongoTemplate.getFieldValueById(id: ObjectId, field: String, clazz: Class<T>): R? {
    val query = Query.query(Criteria.where("id").`is`(id))
    val fields = query.fields()
    fields.include(field)
    return getPropValue<R>(this.findOne(query, clazz), field)
}

fun <R, T> MongoTemplate.getFieldValue(field: String, query: Query, clazz: Class<T>): R? {
    val fields = query.fields()
    fields.include(field)

    return getPropValue<R>(this.findOne(query, clazz), field)
}
/**
 * 获取对象指定的属性的值
 */
@Suppress("UNCHECKED_CAST")
fun <R> getPropValue(instance: Any?, propertyName: String): R? {
    if (instance == null)
        return null
    val property = instance::class.memberProperties.first { it.name == propertyName } as KProperty1<Any, *>
    return property.get(instance) as R
}

fun String.getRegexCriteria(field: String): Criteria {
    val pattern =
        Pattern.compile("^.*${this.trim().escapeSpecialWordForMongoQuery()}.*$", Pattern.CASE_INSENSITIVE)

    return Criteria(field).regex(pattern)
}

/**
 * 防止特殊符号导致mongodb查询出错
 */
fun String.escapeSpecialWordForMongoQuery(): String {
    var keyword = this
    val fbsArr = arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
    for (key in fbsArr) {
        if (keyword.contains(key)) {
            keyword = keyword.replace(key, "\\" + key)
        }
    }
    return keyword
}