package kt.speedy.toolbox.data.jpa.converter

import cn.hutool.json.JSONObject
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class JSONObjectToStringConverter : AttributeConverter<JSONObject, String> {
    override fun convertToDatabaseColumn(attribute: JSONObject?): String? {
        return attribute?.toString()
    }

    override fun convertToEntityAttribute(dbData: String?): JSONObject? {
        return dbData?.let { JSONObject(it) }
    }
}