package ${packageName}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.service.I${className}Service;

@Service
public class ${className}ServiceImpl implements I${className}Service {
	@Autowired
	private ${className}Mapper ${objectName}Mapper;
}
