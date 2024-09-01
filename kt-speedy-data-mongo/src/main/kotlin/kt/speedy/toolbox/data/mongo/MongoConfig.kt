package kt.speedy.toolbox.data.mongo

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.io.IOException


abstract class MongoConfig {
    /**
     * ObjectId 序列化
     */
    @Bean
    open fun jacksonObjectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper? {
        val objectMapper = builder.createXmlMapper(false).build<ObjectMapper>()
        objectMapper.serializerProvider.setNullValueSerializer(object : JsonSerializer<Any?>() {
            @Throws(IOException::class)
            override fun serialize(value: Any?, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeString("")
            }
        })
        val module = SimpleModule()
        module.addSerializer(ObjectId::class.java, ToStringSerializer())
        objectMapper.registerModule(module)
        return objectMapper
    }

    abstract fun mongoAuditorProvider(): AuditorAware<String>

}