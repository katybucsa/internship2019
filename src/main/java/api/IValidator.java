package api;

import repository.ValidationException;

public interface IValidator<Entity> {

    /**
     * validates an entity
     *
     * @param entity the entity to validate
     * @throws ValidationException
     */
    void validate(Entity entity) throws ValidationException;
}
