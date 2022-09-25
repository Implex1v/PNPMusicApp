import {useState} from "react";

type SongSearchTextProp = {
    search: string,
    setSearch,
    submit,
}
const regex = new RegExp("[A-Za-z0-9]+=[A-Za-z0-9]+(,[A-Za-z0-9]+=[A-Za-z0-9]+)*")

export default function SongSearchText({search, setSearch, submit}: SongSearchTextProp) {
    const [valid, setValid] = useState(null)

    const handleOnChange = function (event) {
        const val = event.target.value
        if(!regex.test(val)) {
            setValid(false)
        } else {
            setValid(true)
        }

        setSearch(val)
    }

    const handleOnSubmit = function () {
        if(!valid) {
            return
        }

        setValid(true)
        submit(search)
    }

    return (
        <div>
            {valid == false && <span className="text-danger">Invalid search pattern.</span>}
            <div className="d-flex mt-3 mb-3">
                <input type="text" className="form-control" placeholder="name=foo or tags=city" value={search} onChange={handleOnChange} />
                <button className="btn btn-primary ms-3" onClick={handleOnSubmit} disabled={!valid}>Search</button>
            </div>
        </div>
    )
}