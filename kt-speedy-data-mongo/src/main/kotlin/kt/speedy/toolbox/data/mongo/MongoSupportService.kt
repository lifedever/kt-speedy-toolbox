package kt.speedy.toolbox.data.mongo

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.ArrayOperators
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.*

/**
 * T 为 model类
 */
abstract class MongoSupportService<T : MongoSupportModel>(private val clazz: Class<T>) {
    protected abstract val template: MongoTemplate

    /**
     * 根据Id查询对象，与 getOne 一样
     */
    fun findById(id: ObjectId): T? {
        return template.findById(id, clazz)
    }

    /**
     * 根据Id查询对象，与 getOne 一样
     */
    fun findById(id: ObjectId, fields: List<String>): T? {
        val query = Query.query(
            Criteria.where("id").`is`(id)
        )
        fields.forEach {
            query.fields().include(it)
        }
        return template.findById(id, clazz)
    }

    /**
     * 根据id查询对象，与 findById 一样
     */
    fun getOne(id: ObjectId): T? {
        return findById(id)
    }

    /**
     * 根据 属性 == 值 返回 一个对象
     */
    fun getOneByFieldIs(field: String, isValue: Any?): T? {
        return template.findOne(
            Query.query(
                Criteria.where(field).`is`(isValue)
            ),
            clazz
        )
    }

    /**
     * 根据 属性 == 值 返回 一个对象
     */
    fun findByFieldIs(field: String, isValue: Any?): List<T> {
        return template.find(
            Query.query(
                Criteria.where(field).`is`(isValue)
            ),
            clazz
        )
    }

    /**
     * 根据 属性 == 值 返回 一个对象
     */
    fun findByFieldIs(field: String, isValue: Any?, includeFields: List<String>): List<T> {
        val query = Query.query(
            Criteria.where(field).`is`(isValue)
        )
        includeFields.forEach {
            query.fields().include(it)
        }
        return template.find(
            query,
            clazz
        )
    }

    /**
     * 根据 属性 == 值 返回 一个对象
     */
    fun findByFieldIs(field: String, isValue: Any?, sort: Sort): List<T> {
        val query = Query.query(
            Criteria.where(field).`is`(isValue)
        )
        query.with(sort)
        return template.find(query, clazz)
    }

    /**
     * 根据 属性 == 值 返回 一个对象
     */
    fun countByFieldIs(field: String, isValue: Any?, deleted: Boolean? = false): Long {
        val criteria = Criteria.where(field).`is`(isValue).withDeleted(deleted)

        val query = Query.query(
            criteria
        )

        return template.count(query, clazz)
    }

    /**
     * 返回所有数据，为了效率，慎用
     */
    fun findAll(): List<T> {
        return template.findAll(clazz)
    }

    /**
     * 返回所有数据
     */
    fun findAll(query: Query): List<T> {
        return template.find(query, clazz)
    }
    /**
     * 返回所有数据
     */
    fun findAllWith(query: Query): List<T> {
        return template.find(query, clazz)
    }

    /**
     * 返回所有数据，带排序
     */
    fun findAll(sort: Sort): List<T> {
        val query = Query()
        query.with(sort)
        return template.find(query, clazz)
    }

    /**
     * 返回所有数据，带排序还条件查询
     */
    fun findAll(sort: Sort, query: Query): List<T> {
        query.with(sort)
        return template.find(query, clazz)
    }

    /**
     * 返回所有数据，结果分页
     */
    fun findAll(page: PageRequest): Page<T> {
        val query = Query()
        query.with(page)

        val content = template.find(query, clazz)
        val total = count(query)

        return PageImpl<T>(content, page, total)
    }

    /**
     * 返回所有数据，带查询，结果分页
     */
    fun findAll(page: PageRequest, query: Query): Page<T> {
        val total = count(query)
        query.with(page)
        val content = template.find(query, clazz)

        return PageImpl<T>(content, page, total)
    }

    fun findOne(query: Query): T? {
        return template.findOne(query, clazz)
    }

    fun findByIds(ids: List<ObjectId>, includeFields: List<String>? = listOf()): List<T> {
        val query = Query.query(
            Criteria.where("id").`in`(ids)
        )
        includeFields?.forEach {
            query.fields().include(it)

        }
        return template.find(
            query,
            clazz
        )
    }

    /**
     * 获取一条数据的一个属性，只返回属性的值，提高查询效率
     * @param id 数据主键
     * @param field 要查询的字段名称
     */
    fun <R> getFieldValueById(id: ObjectId, field: String): R? {
        val query = Query.query(Criteria.where("id").`is`(id))
        val fields = query.fields()
        fields.include(field)

        return getPropValue<R>(template.findOne(query, clazz), field)
    }

    /**
     * 获取一条数据的一个属性，只返回属性的值，提高查询效率
     */
    fun <R> getFieldValue(field: String, query: Query): R? {
        val fields = query.fields()
        fields.include(field)

        return getPropValue<R>(template.findOne(query, clazz), field)
    }

    /* ---- findByDBRef  ------*/

    /**
     * 返回所有数据，根据DBRef查询
     */
    fun findByDBRef(refName: String, refId: ObjectId): List<T> {
        val query = Query.query(Criteria.where("$refName.id").`is`(refId))
        return template.find(query, clazz)
    }

    /**
     * 返回所有数据，根据DBRef查询，带排序
     */
    fun findByDBRef(refName: String, refId: ObjectId, sort: Sort): List<T> {
        val query = Query.query(Criteria.where("$refName.id").`is`(refId))
        query.with(sort)
        return template.find(query, clazz)
    }

    /**
     * 返回所有数据，根据DBRef查询，结果分页
     */
    fun findByDBRef(refName: String, refId: ObjectId, page: PageRequest): Page<T> {
        val query = Query.query(Criteria.where("$refName.id").`is`(refId))
        query.with(page)

        val content = template.find(query, clazz)
        val total = count(query)

        return PageImpl<T>(content, page, total)
    }

    /**
     * 返回数量
     */
    fun count(): Long {
        return template.count(Query(), clazz)
    }

    /**
     * 返回数量，带查询
     */
    fun count(query: Query): Long {
        return template.count(query, clazz)
    }

    /**
     * 返回数组字段的长度
     */
    fun countArrayField(id: ObjectId, field: String): Int {
        val agg = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where("id").`is`(id)
            ),
            Aggregation.project()
                .and(ArrayOperators.Size.lengthOfArray(ConditionalOperators.ifNull(field).then(listOf<Int>())))
                .`as`("count")

        )
        return template.aggregate(agg, clazz, HashMap::class.java).mappedResults.first()["count"] as Int
    }

    /* ---- save or update  ------*/

    /**
     * 保存一个对象
     */
    fun save(t: T): T {
        return template.save(t)
    }

    fun updateFirst(query: Query, update: Update): UpdateResult {
        return template.updateFirst(query, update, clazz)
    }

    fun updateMulti(query: Query, update: Update): UpdateResult {
        return template.updateMulti(query, update, clazz)
    }

    /**
     * 根据Id更新一条数据
     */
    @Throws(Exception::class)
    fun updateById(id: ObjectId, update: Update): T {
        template.updateFirst(Query.query(Criteria.where("id").`is`(id)), update, clazz)
        return findById(id) ?: throw Exception("Object for Update not exists!")
    }

    /**
     * add To Set
     */
    fun addToSet(id: ObjectId, key: String, value: Any): T {
        return this.updateById(id, Update().addToSet(key, value))
    }

    /**
     * add To Set
     */
    fun pull(id: ObjectId, key: String, value: Any): T {
        return this.updateById(id, Update().pull(key, value))
    }

    /**
     * 根据id更新一条数据的一个属性，采用 set 或 unset 操作
     */
    @Throws(Exception::class)
    fun updateFieldById(id: ObjectId, key: String, value: Any?): T {
        return updateById(id, Update().setOrUnset(key, value))
    }

    fun unsetFieldById(id: ObjectId, key: String): T {
        return updateById(id, Update().unset(key))
    }

    fun remove(t: T): DeleteResult {
        return template.remove(t)
    }

    fun remove(query: Query): DeleteResult {
        return template.remove(query, clazz)
    }

    fun deleteById(id: ObjectId) {
        template.deleteById(id, clazz)
    }

    /**
     * 逻辑删除
     * @param id ObjectId
     */
    fun deleteByIdLogic(id: ObjectId) {
        updateFieldById(id, "deleted", true)
    }

    fun getCreatedDate(id: ObjectId): Date? {
        return this.getFieldValueById(id, "createdDate")
    }
}
