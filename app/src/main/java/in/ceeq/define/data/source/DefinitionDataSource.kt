package `in`.ceeq.define.data.source

import `in`.ceeq.define.data.entity.Definition

interface DefinitionDataSource {
    fun getDefinition(phrase: String): Definition
}
