namespace Kolos2.Services;

public interface IDbService
{
    Task<bool> DoesCharExist(int id);
    Task<object> GetChar(int id);
    Task<bool> DoesItemExist(int id);
    Task<bool> DoesCharHaveEnoughtWeight(int id, List<int> newIdItems);
    Task AddItemsToBackpack(int id, List<int> newIdItems);
    Task<List<object>> GetCharactersItems(int id);
}