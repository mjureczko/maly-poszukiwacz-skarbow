package pl.marianjureczko.poszukiwacz.shared.port.storage

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.someDouble

class AveragedLocationXmlArranger : CustomArranger<AveragedLocationXml>() {

    override fun instance(): AveragedLocationXml {
        return AveragedLocationXml(someDouble(), someDouble())
    }
}